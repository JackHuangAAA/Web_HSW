#!/bin/bash

if [[ $# -ne 2 ]]
then
    echo "Invalid number of arguments! Use: '$0 <executable> <description word>'"
    exit
fi

exec_name=$1
exec_path="$(pwd)/$exec_name"
service_dir="/lib/systemd/system/"
description=$2

if [[ $(id -u) != "0" ]]
then
    echo "You are not root!"
    exit
fi

if [[ ! -x "$exec_path" ]]
then
    echo "File '$exec_path' is not executable or does not exist!"
    exit
fi

echo "[Unit]" > "$service_dir$exec_name.service"
echo "Description=$description" >> "$service_dir$exec_name.service"
echo "" >> "$service_dir$exec_name.service"
echo "[Service]" >> "$service_dir$exec_name.service"
echo "ExecStart=$exec_path" >> "$service_dir$exec_name.service"
echo "WorkingDirectory=$(pwd)" >> "$service_dir$exec_name.service"
echo "Environment=DISPLAY=:0" >> "$service_dir$exec_name.service"
echo "" >> "$service_dir$exec_name.service"
echo "[Install]" >> "$service_dir$exec_name.service"
echo "WantedBy=graphical.target" >> "$service_dir$exec_name.service"

systemctl daemon-reload
systemctl enable "$exec_name.service"

echo "Service has been created and enabled successfully! Please reboot."