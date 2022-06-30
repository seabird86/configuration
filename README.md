# CONFIGURATION
 
## KAFKA (For Spring bus)

```
docker run -d -p 2181:2181 --net=anh-network --name zookeeper --hostname localhost -e ALLOW_ANONYMOUS_LOGIN=yes bitnami/zookeeper:latest

docker run -d -p 9092:9092 --net=anh-network -p 29092:29092 --name kafka --hostname localhost -e KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper:2181 -e KAFKA_CFG_ADVERTISED_LISTENERS=CLIENT://kafka:29092,EXTERNAL://localhost:9092, -e KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CLIENT:PLAINTEXT,EXTERNAL:PLAINTEXT -e KAFKA_INTER_BROKER_LISTENER_NAME=CLIENT -e KAFKA_CFG_LISTENERS=CLIENT://:29092,EXTERNAL://:9092 -e ALLOW_PLAINTEXT_LISTENER=yes bitnami/kafka:latest
```

```
docker run -d -p 8200:8200 --net=anh-network --name vault --cap-add=IPC_LOCK -e 'VAULT_DEV_ROOT_TOKEN_ID=token_id' -e 'VAULT_DEV_LISTEN_ADDRESS=0.0.0.0:8200' vault
```

## Visual Studio

Plugins:
- Spring dasdboard


- User Setting:
```
docker.toolbarLocation=hide
```




- Endpoints:

```
curl --location --request GET 'http://localhost:7000/configuration/actuator' --header 'Cookie: JSESSIONID=78FC49F696984D5B0C8B84EB543A81FF'

curl --location --request GET 'http://localhost:7000/configuration/config/customer/jdbc' --header 'Cookie: JSESSIONID=78FC49F696984D5B0C8B84EB543A81FF'

/{application}/{profile}[/{label}]
/{application}-{profile}.yml
/{label}/{application}-{profile}.yml
/{application}-{profile}.properties
/{label}/{application}-{profile}.properties
```

### 2. Configuration Security

##### 1) Server side

- Add username/password

```
spring:
	security:
	    user:
	      name: ANHNT
	      password: ANHNT-PASS
```
- Client call with Authorization Basic

```
curl --location --request GET 'http://localhost:7000/configuration/config/customer/jdbc' \
--header 'Authorization: Basic QU5ITlQ6QU5ITlQtUEFTUw=='
```
- Server will add cookie in response

```
Set-Cookie: JSESSIONID=A9C1E46A0C0209BB7DDF7254B98474F3; Path=/configuration; HttpOnly
```
- Next request send `Cookie: JSESSIONID=A9C1E46A0C0209BB7DDF7254B98474F3` and doesn't need `Authorization` header

##### 2) Client side

```
spring:
	config:
		import: "optional:configserver:http://ANHNT:ANHNT-PASS@localhost:7000/configuration/config"
```

### 3. Encrypt Value in configuration

##### 1) Server Side

- Run in terminal to generate file config-server.jks:


```
keytool -genkeypair -alias config-server-key \
       -keyalg RSA -keysize 4096 -sigalg SHA512withRSA \
       -dname 'CN=Config Server,OU=Spring Cloud,O=Baeldung' \
       -keypass my-k34-s3cr3t -keystore config-server.jks \
       -storepass my-s70r3-s3cr3t
```

- Move file config-server.jks into folder `main/resources`
- Add these config into application.yml

```
encrypt.keyStore.location=classpath:/config-server.jks
encrypt.keyStore.password=my-s70r3-s3cr3t
encrypt.keyStore.alias=config-server-key
encrypt.keyStore.secret=my-k34-s3cr3t
```

- Encrypt ${value} into ${encode-value}:

```
curl --location --request POST 'http://localhost:7000/configuration/config/encrypt' \
--header 'Authorization: Basic QU5ITlQ6QU5ITlQtUEFTUw==' \
--header 'Content-Type: text/plain' \
--data-raw 'Welcome1'
```
- Reponse as ${encode-value}:

```
AgBGGd2aC86+DTdiQbNRpZ18eyH2W8Aa3k5pAdMBgTaVLCfe1OaUtMvOeWQWRB3OMBvuShRiObmkR9q8zmsdB83YQj/L+Jzmu2vpCLciSX3hLhxC4NyB8kVKNtnYTUfXS9RaKvNH6dChvCSJ/DpYBzeH4nBVm0ZoFTZ3KnD9OCIJqD9uzanFJKLtbHyzl0nJ3tDhulKW90C8OLcayIpLAd83RXBiIMHpKV7pyOAB6IuNm+b0Mf4XnYehxd8Ba1AyE6hyCKQwS44H7mmGdZa0wV83cCf+mPH5GnQ+SKELGBcZerO9d+E2bE8cU2aZf7NSQOI8MwPWjXCboiviYypWCliQM9dZKL9phovoOJPsKG6nYTX1DMkNbzl2zobYu/z7q2rcrtCzJq8OrfolcOAXIhe9FsJL4Gisv84kJP2UdNacjSWRotGRj3I8Mw6Y/7XzTZxmfmmeQTTCF8mZWIrmHUO2ZeKlQVKklfj5kLo9ngGfA8XI+0FfiTewaELdbY14XVNOaTWPWR5Yg2uYI63oAyLVi0NyT+tdOIGYRtJjS96Xo5ryBkOEFqDR4e3E/Y4aYYHqQtYhy2r8ymYxbcrz8osw3XRwpNmGKnwxGh4p15B7OHb39B1Z47F/KeQxZIju7ZcBVqDpBzKaQJeBPJXu6k2oKleCU3MSdqrRSi5NZ21gH1NS370IQ5LLZfxp6FrwpgogGBkxBc3U3i5XiCtDtp6L
```
- Decrypt ${encode-value} into ${value}:

```
curl --location --request POST 'http://localhost:7000/configuration/config/decrypt' \
--header 'Content-Type: text/plain' \
--header 'Authorization: Basic QU5ITlQ6QU5ITlQtUEFTUw==' \
--data-raw 'AgBGGd2aC86+DTdiQbNRpZ18eyH2W8Aa3k5pAdMBgTaVLCfe1OaUtMvOeWQWRB3OMBvuShRiObmkR9q8zmsdB83YQj/L+Jzmu2vpCLciSX3hLhxC4NyB8kVKNtnYTUfXS9RaKvNH6dChvCSJ/DpYBzeH4nBVm0ZoFTZ3KnD9OCIJqD9uzanFJKLtbHyzl0nJ3tDhulKW90C8OLcayIpLAd83RXBiIMHpKV7pyOAB6IuNm+b0Mf4XnYehxd8Ba1AyE6hyCKQwS44H7mmGdZa0wV83cCf+mPH5GnQ+SKELGBcZerO9d+E2bE8cU2aZf7NSQOI8MwPWjXCboiviYypWCliQM9dZKL9phovoOJPsKG6nYTX1DMkNbzl2zobYu/z7q2rcrtCzJq8OrfolcOAXIhe9FsJL4Gisv84kJP2UdNacjSWRotGRj3I8Mw6Y/7XzTZxmfmmeQTTCF8mZWIrmHUO2ZeKlQVKklfj5kLo9ngGfA8XI+0FfiTewaELdbY14XVNOaTWPWR5Yg2uYI63oAyLVi0NyT+tdOIGYRtJjS96Xo5ryBkOEFqDR4e3E/Y4aYYHqQtYhy2r8ymYxbcrz8osw3XRwpNmGKnwxGh4p15B7OHb39B1Z47F/KeQxZIju7ZcBVqDpBzKaQJeBPJXu6k2oKleCU3MSdqrRSi5NZ21gH1NS370IQ5LLZfxp6FrwpgogGBkxBc3U3i5XiCtDtp6L'
```
- Add to table property value: 

```
('application','default','master','spring.datasource.password',"{cipher}AgBGGd2aC86+DTdiQbNRpZ18eyH2W8Aa3k5pAdMBgTaVLCfe1OaUtMvOeWQWRB3OMBvuShRiObmkR9q8zmsdB83YQj/L+Jzmu2vpCLciSX3hLhxC4NyB8kVKNtnYTUfXS9RaKvNH6dChvCSJ/DpYBzeH4nBVm0ZoFTZ3KnD9OCIJqD9uzanFJKLtbHyzl0nJ3tDhulKW90C8OLcayIpLAd83RXBiIMHpKV7pyOAB6IuNm+b0Mf4XnYehxd8Ba1AyE6hyCKQwS44H7mmGdZa0wV83cCf+mPH5GnQ+SKELGBcZerO9d+E2bE8cU2aZf7NSQOI8MwPWjXCboiviYypWCliQM9dZKL9phovoOJPsKG6nYTX1DMkNbzl2zobYu/z7q2rcrtCzJq8OrfolcOAXIhe9FsJL4Gisv84kJP2UdNacjSWRotGRj3I8Mw6Y/7XzTZxmfmmeQTTCF8mZWIrmHUO2ZeKlQVKklfj5kLo9ngGfA8XI+0FfiTewaELdbY14XVNOaTWPWR5Yg2uYI63oAyLVi0NyT+tdOIGYRtJjS96Xo5ryBkOEFqDR4e3E/Y4aYYHqQtYhy2r8ymYxbcrz8osw3XRwpNmGKnwxGh4p15B7OHb39B1Z47F/KeQxZIju7ZcBVqDpBzKaQJeBPJXu6k2oKleCU3MSdqrRSi5NZ21gH1NS370IQ5LLZfxp6FrwpgogGBkxBc3U3i5XiCtDtp6L")
```

### 4. Cross-Site Request Forgery

##### 1) Server Side
- Set configuration:

```
http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
```
- Request to server, server will respond with 

```
Set-Cookie: XSRF-TOKEN=d8466816-0ec6-420c-a911-e411fa425682; Path=/configuration
```
- Next request with header: 

```
X-XSRF-TOKEN=d8466816-0ec6-420c-a911-e411fa425682
```

**Reference links:**

1. [https://www.baeldung.com/spring-cloud-configuration](https://www.baeldung.com/spring-cloud-configuration)
2. [https://www.baeldung.com/spring-security-csrf](https://www.baeldung.com/spring-security-csrf)
3. [https://www.baeldung.com/csrf-stateless-rest-api](https://www.baeldung.com/csrf-stateless-rest-api)
4. [https://www.baeldung.com/spring-prevent-xss](https://www.baeldung.com/spring-prevent-xss)