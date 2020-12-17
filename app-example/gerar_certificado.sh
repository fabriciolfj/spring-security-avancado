openssl req -newkey rsa:2048 -x509 -keyout key.pem -out cert.pem -days 365

#gerando um certificado auto assinado
openssl pkcs12 -export -in cert.pem -inkey key.pem -out certificate.p12 -name "certificate"
