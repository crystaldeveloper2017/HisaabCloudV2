#!/bin/bash

# Function to check if Docker is installed
check_docker_installed() {
    if command -v docker &>/dev/null; then
        return 0  # Docker is installed
    else
        return 1  # Docker is not installed
    fi
}

# Function to install Docker
install_docker() {
    echo "Docker is not installed. Installing Docker..."
    # Add the Docker repository and install Docker
    sudo apt update
    sudo apt install -y apt-transport-https ca-certificates curl software-properties-common
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
    echo "deb [signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
    sudo apt update
    sudo apt install -y docker-ce docker-ce-cli containerd.io

    # Add the current user to the docker group
    sudo usermod -aG docker $USER

    echo "Docker has been installed successfully."
}

# Function to run the MySQL container
run_mysql_container() {
    echo "Running MySQL container..."
    sudo docker run --name mysqldb-container -d -p 3305:3306 -e MYSQL_ROOT_PASSWORD=root mysql:8.0.33-debian
    echo "MySQL container has been started."
}

# Function to run the hisaabcloud container
run_hisaabcloud_container() {
    echo "Running Hisaab Cloud container..."
    sudo docker run -d --link mysqldb-container -p 8080:8080 \
        -e mysqlusername="root" \
        -e password="root" \
        -e host="mysqldb-container" \
        -e port="3306" \
        -e mySqlPath="1" \
        -e schemaName="customizedpos_local" \
        -e projectName="Hisaab Cloud Offline" \
        -e thread_sleep=0 \
        -e isAuditEnabled="false" \
        -e copyAttachmentsToBuffer="false" \
        -e persistentPath="/home/ubuntu/society_maintenance_staging/" \
        -e queryLogEnabled="false" \
        -e sendEmail="false" \
        crystaldevelopers2017/hisaabcloud
    echo "Hisaab Cloud container has been started."
}

wait_for_mysql() {
    local container_name="mysqldb-container"
    local max_attempts=30
    local sleep_duration=1
    local i=1

    echo "Waiting for MySQL container to start..."
    while [ "$i" -le "$max_attempts" ]; do
        if [sudo docker inspect -f '{{.State.Running}}' "$container_name" 2>/dev/null | grep -q "true"]; then
            echo "MySQL container is ready."
            return 0
        else
            echo "mysql container isn't running yet"
            i=$((i+1))
            sleep $sleep_duration
        fi

    done

    echo "Timed out waiting for MySQL container to start."
    return 1
}

# Check if Docker is installed
if check_docker_installed; then
    echo "Docker is already installed."
else
    install_docker
fi

# Run the MySQL container
run_mysql_container

wait_for_mysql

# Run the Hisaab Cloud container
run_hisaabcloud_container
