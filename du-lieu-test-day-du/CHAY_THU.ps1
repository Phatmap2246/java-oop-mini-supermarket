$ErrorActionPreference = 'Stop'
$gocDoAn = Split-Path -Parent $PSScriptRoot
$lopThu = Join-Path $PSScriptRoot 'classes'
$thuVien = Join-Path $gocDoAn 'lib/*'
$nguonJava = @(Get-ChildItem -LiteralPath (Join-Path $gocDoAn 'src') -Recurse -Filter *.java | ForEach-Object { $_.FullName })
& javac -encoding UTF-8 -cp $thuVien -d $lopThu $nguonJava
if ($LASTEXITCODE -ne 0) { throw 'Biên dịch thất bại.' }
Push-Location $PSScriptRoot
try { & java -cp "$lopThu;$thuVien" Main } finally { Pop-Location }
