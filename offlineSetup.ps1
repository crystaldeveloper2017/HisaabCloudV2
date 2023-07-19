# Function to check if Docker is installed
function Check-DockerInstalled {
    if (Get-Command docker -ErrorAction SilentlyContinue) {
        return $true  # Docker is installed
    } else {
        return $false  # Docker is not installed
    }
}

# Function to install Docker
function Install-Docker {
    Write-Host "Docker is not installed. Installing Docker..."

    # Install Docker using Chocolatey (a popular package manager for Windows)
    Set-ExecutionPolicy Bypass -Scope Process -Force; iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))
    choco install docker -y

    Write-Host "Docker has been installed successfully."
}

# Function to run the MySQL container
function Run-MySQLContainer {
    Write-Host "Running MySQL container..."
    docker run --name mysqldb-container -d -p 3305:3306 -e MYSQL_ROOT_PASSWORD=root mysql:8.0.33-debian
    Write-Host "MySQL container has been started."
}

# Function to run the Hisaab Cloud container
function Run-HisaabCloudContainer {
    Write-Host "Running Hisaab Cloud container..."
    docker run -d --link mysqldb-container -p 8080:8080 `
        -e mysqlusername="root" `
        -e password="root" `
        -e host="mysqldb-container" `
        -e port="3306" `
        -e mySqlPath="1" `
        -e schemaName="customizedpos_local" `
        -e projectName="Hisaab Cloud Offline" `
        -e thread_sleep=0 `
        -e isAuditEnabled="false" `
        -e copyAttachmentsToBuffer="false" `
        -e persistentPath="/home/ubuntu/society_maintenance_staging/" `
        -e queryLogEnabled="false" `
        -e sendEmail="false" `
        crystaldevelopers2017/hisaabcloud
    Write-Host "Hisaab Cloud container has been started."
}

# Function to wait until MySQL container is running
function Wait-ForMySQL {
    $containerName = "mysqldb-container"
    $maxAttempts = 30
    $sleepDuration = 1
    $i = 1

    Write-Host "Waiting for MySQL container to start..."
    while ($i -le $maxAttempts) {
        $running = (docker inspect -f '{{.State.Running}}' $containerName 2> $null) -eq "true"
        if ($running) {
            Write-Host "MySQL container is ready."
            return
        } else {
            Write-Host "MySQL container isn't running yet."
            $i++
            Start-Sleep -Seconds $sleepDuration
        }
    }

    Write-Host "Timed out waiting for MySQL container to start."
    throw "MySQL container not ready."
}

# Check if Docker is installed
if (Check-DockerInstalled) {
    Write-Host "Docker is already installed."
} else {
    Install-Docker
}

# Run the MySQL container
Run-MySQLContainer

# Wait for the MySQL container to start
Wait-ForMySQL

# Run the Hisaab Cloud container
Run-HisaabCloudContainer
