FROM golang:alpine as build-step
WORKDIR /fizzbuzz
COPY . .
RUN go mod init test
RUN go build -v -o FizzBuzzApp

FROM alpine
WORKDIR /app
COPY --from=build-step /fizzbuzz/FizzBuzzApp .
CMD ["./FizzBuzzApp"]
