case $1 in
  "build")

    echo "creating compiler container"

    docker build -t jarc:latest .

    echo "created image"

    docker run -v $(pwd):/home/compiler/ jarc

    docker rmi jarc --force

    echo "compiled!"

  ;;
  "run")
    docker-compose -p "microservices" up -d --force-recreate --build
;;
esac