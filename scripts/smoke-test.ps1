$ErrorActionPreference = "Stop"
$base = "http://localhost:8085"
$outDir = Join-Path $PSScriptRoot "..\evidencias"
New-Item -ItemType Directory -Force -Path $outDir | Out-Null
$suffix = Get-Date -Format "HHmmss"

function Save-Json($name, $obj) {
    $path = Join-Path $outDir "$name.json"
    $json = $obj | ConvertTo-Json -Depth 10
    [System.IO.File]::WriteAllText($path, $json, (New-Object System.Text.UTF8Encoding($false)))
    Write-Host "OK -> $name.json"
}

function Invoke-Api($method, $url, $body, $token) {
    $headers = @{}
    if ($token) { $headers["Authorization"] = "Bearer $token" }
    $params = @{
        Method          = $method
        Uri             = "$base$url"
        Headers         = $headers
        UseBasicParsing = $true
    }
    if ($null -ne $body) {
        $json = $body | ConvertTo-Json -Depth 10
        $params["ContentType"] = "application/json; charset=utf-8"
        $params["Body"] = [System.Text.Encoding]::UTF8.GetBytes($json)
    }
    $resp = Invoke-WebRequest @params
    $text = [System.Text.Encoding]::UTF8.GetString($resp.RawContentStream.ToArray())
    if ($text) { return $text | ConvertFrom-Json }
}

function PostJson($url, $body, $token) {
    return Invoke-Api "Post" $url $body $token
}

function GetJson($url, $token) {
    return Invoke-Api "Get" $url $null $token
}

$login = PostJson "/login" @{ login = "admin"; senha = "admin" } $null
Save-Json "01-login-admin" $login
$token = $login.tokenJWT

$ong = PostJson "/ongs" @{
    nome = "Instituto Recomeço $suffix"
    email = "contato.$suffix@recomeco.org.br"
    telefone = "11 99999-0000"
} $token
Save-Json "02-cadastro-ong" $ong

$userJovem = PostJson "/usuarios" @{
    login = "jovem.maria.$suffix"
    senha = "jovem123"
    perfil = "JOVEM"
} $token
Save-Json "03-cadastro-usuario-jovem" $userJovem

$jovem = PostJson "/jovens" @{
    nome = "Maria Silva"
    email = "maria.$suffix@email.com"
    telefone = "11 97777-0000"
    tamanho = "M"
    id_ong = $ong.id
    endereco = @{
        logradouro = "Rua das Flores"
        numero = "100"
        complemento = $null
        bairro = "Centro"
        cidade = "São Paulo"
        uf = "SP"
        cep = "01000-000"
    }
    consentimento_aceito = $true
} $token
Save-Json "04-cadastro-jovem" $jovem

$jovemValidado = PostJson "/jovens/$($jovem.id)/validar" @{} $token
Save-Json "05-validar-jovem" $jovemValidado

$peca1 = PostJson "/pecas" @{
    nome = "Camisa social branca"
    categoria = "CAMISA"
    tamanho = "M"
    estado_conservacao = "NOVO"
    foto_url = "https://exemplo.com/camisa.jpg"
    local_armazem = "A1"
    origem = "Doação"
} $token
Save-Json "06-cadastro-peca-1" $peca1

$peca2 = PostJson "/pecas" @{
    nome = "Calça social preta"
    categoria = "CALCA"
    tamanho = "M"
    estado_conservacao = "BOM"
    foto_url = "https://exemplo.com/calca.jpg"
    local_armazem = "A2"
    origem = "Doação"
} $token
Save-Json "07-cadastro-peca-2" $peca2

$loginJovem = PostJson "/login" @{ login = "jovem.maria.$suffix"; senha = "jovem123" } $null
Save-Json "08-login-jovem" $loginJovem
$tokenJovem = $loginJovem.tokenJWT

$pedido = PostJson "/pedidos" @{
    id_jovem = $jovem.id
    itens = @(
        @{ id_peca = $peca1.id },
        @{ id_peca = $peca2.id }
    )
} $tokenJovem
Save-Json "09-criar-pedido" $pedido

$aprovado = PostJson "/pedidos/$($pedido.id)/aprovar" @{} $token
Save-Json "10-aprovar-pedido" $aprovado

$despachado = PostJson "/pedidos/$($pedido.id)/despachar" @{
    entregador = "Motoboy Carlos"
    custo_entrega = 30.00
} $token
Save-Json "11-despachar-pedido" $despachado

$entregue = PostJson "/pedidos/$($pedido.id)/entregar" @{} $token
Save-Json "12-entregar-pedido" $entregue

$feedback = PostJson "/pedidos/$($pedido.id)/feedback" @{
    itens = @(
        @{ id_item = $pedido.itens[0].id; serviu = $true },
        @{ id_item = $pedido.itens[1].id; serviu = $true }
    )
    foi_entrevista = $true
    comentario = "As peças caíram muito bem na entrevista!"
} $tokenJovem
Save-Json "13-feedback-pedido" $feedback

$acervo = GetJson "/pecas?status=DISPONIVEL" $token
Save-Json "14-consultar-acervo-disponivel" $acervo

$pedidos = GetJson "/pedidos?ong=$($ong.id)" $token
Save-Json "15-consultar-pedidos-ong" $pedidos

Write-Host "Smoke test concluido com sucesso."
