#!/bin/bash

# Pfade zum Frontend und Backend
FRONTEND_DIR="/lecture-connect/Lecture-Connect/Frontend"
BACKEND_DIR="/lecture-connect/Lecture-Connect"

# Log-Dateien für Frontend und Backend
FRONTEND_LOG="/var/log/lecture-connect-frontend.log"
BACKEND_LOG="/var/log/lecture-connect-backend.log"

# Datei zum Speichern der PIDs
PID_FILE="./app-pids.txt"

# Funktion zum Starten des Frontends
start_frontend() {
    echo "Starte Frontend im Hintergrund..."
    cd "$FRONTEND_DIR"
    nohup ng serve > "$FRONTEND_LOG" 2>&1 &
    FRONTEND_PID=$!
    echo "Frontend gestartet mit PID $FRONTEND_PID. Logs: $FRONTEND_LOG"
}

# Funktion zum Starten des Backends
start_backend() {
    echo "Starte Backend im Hintergrund..."
    cd "$BACKEND_DIR"
    nohup ./mvnw spring-boot:run > "$BACKEND_LOG" 2>&1 &
    BACKEND_PID=$!
     echo "Backend gestartet mit PID $BACKEND_PID. Logs: $BACKEND_LOG"
}

# Funktion zum Starten der Anwendungen
start_app() {
    start_frontend
    start_backend

    cd "$BACKEND_DIR/scripts"

    echo "$FRONTEND_PID" > $PID_FILE
    echo "$BACKEND_PID" >> $PID_FILE

    echo "Beide Anwendungen wurden im Hintergrund gestartet."
    echo "Frontend PID: $FRONTEND_PID"
    echo "Backend PID: $BACKEND_PID"
}

# Funktion zum Stoppen der Anwendungen
stop_app() {
    if [ ! -s "$PID_FILE" ]; then
        echo "Keine laufenden Prozesse gefunden."
        exit 1
    fi

    while IFS= read -r pid; do
        if ps -p $pid > /dev/null 2>&1; then
            echo "Stoppe Prozess mit PID $pid..."
            kill $pid
        else
            echo "Prozess mit PID $pid läuft nicht."
        fi
    done < "$PID_FILE"

    # PID-File leeren
    > "$PID_FILE"
    echo "Alle Prozesse wurden gestoppt und die PID-Datei geleert."
}

if [ "$1" == "start" ]; then
    start_app
elif [ "$1" == "stop" ]; then
    stop_app
else
    echo "Verwendung: ./app-controller.sh {start|stop}"
    exit 1
fi
