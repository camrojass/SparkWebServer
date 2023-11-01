# SparkWebServer Taller 1
Primer repositorio para la clase de maestría AYGO de la Escuela Colombiana de Ingeniería
URL Github: https://github.com/camrojass/SparkWebServer.git

## Descripcion Inicial
El taller consiste en crear una aplicación web pequeña usando el micro-framework de Spark java (http://sparkjava.com/). Una vez tengamos esta aplicación procederemos a construir un container para docker para la aplicación y los desplegaremos y configuraremos en nuestra máquina local. Luego, cerremos un repositorio en DockerHub y subiremos la imagen al repositorio. Finalmente, crearemos una máquina virtual de en AWS, instalaremos Docker , y desplegaremos el contenedor que acabamos de crear.

### Primera parte crear la aplicación web
1. Cree un proyecto java usando maven.
2. Cree la clase principal

```
package co.edu.escuelaing.sparkdockerdemolive;

import static spark.Spark.*;

public class SparkWebServer {
    
    public static void main(String... args){
          port(getPort());
          get("hello", (req,res) -> "Hello Docker!");
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }
    
}
```
3. Actualizar las dependencias de spark Java en el archivo pom [pom.xml](https://github.com/camrojass/SparkWebServer/blob/main/pom.xml)

```
<dependencies>
        <!-- https://mvnrepository.com/artifact/com.sparkjava/spark-core -->
        <dependency>
            <groupId>com.sparkjava</groupId>
            <artifactId>spark-core</artifactId>
            <version>2.9.2</version>
        </dependency>
    </dependencies>
```
4. Asegúrese que el proyecto esté compilando hacia la versión 8 de Java
```
<properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
</properties>
```
5. Asegúrese que el proyecto este copiando las dependencias en el directorio target al compilar el proyecto. Esto es necesario para poder construir una imagen de contenedor de docker usando los archivos ya compilados de java. Para hacer esto use el purgan de dependencias de Maven.

```
    <!-- build configuration -->
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <version>3.0.1</version>
                <executions>
                    <execution>
                        <id>copy-dependencies</id>
                        <phase>package</phase>
                        <goals><goal>copy-dependencies</goal></goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```
6. Asegúrese que el proyecto compila
```
$> mvn clean install
```
Debería obtener algo como esto:
```
[INFO] Scanning for projects...
[INFO] 
[INFO] --------< co.edu.escuelaing.sparkdockerdemolive:sparkwebserver >--------                                                                                          
[INFO] Building sparkwebserver 1.0                                                                                                                                       
[INFO]   from pom.xml                                                                                                                                                    
[INFO] --------------------------------[ jar ]---------------------------------                                                                                          
[INFO] 
[INFO] --- clean:3.2.0:clean (default-clean) @ sparkwebserver ---                                                                                                        
[INFO] Deleting C:\Users\camar\OneDrive\Documentos\NetBeansProjects\SparkWebServer\target
[INFO] 
[INFO] --- resources:3.3.1:resources (default-resources) @ sparkwebserver ---                                                                                            
[INFO] Copying 1 resource from src\main\resources to target\classes
[INFO]                                                                                                                                                                   
[INFO] --- compiler:3.11.0:compile (default-compile) @ sparkwebserver ---                                                                                                
[INFO] Changes detected - recompiling the module! :source
[INFO] Compiling 1 source file with javac [debug target 8] to target\classes                                                                                             
[WARNING] bootstrap class path not set in conjunction with -source 8
[INFO]                                                                                                                                                                   
[INFO] --- resources:3.3.1:testResources (default-testResources) @ sparkwebserver ---                                                                                    
[INFO] skip non existing resourceDirectory C:\Users\camar\OneDrive\Documentos\NetBeansProjects\SparkWebServer\src\test\resources                                         
[INFO]                                                                                                                                                                   
[INFO] --- compiler:3.11.0:testCompile (default-testCompile) @ sparkwebserver ---                                                                                        
[INFO] Changes detected - recompiling the module! :dependency                                                                                                            
[INFO] 
[INFO] --- surefire:3.1.2:test (default-test) @ sparkwebserver ---                                                                                                       
[INFO] 
[INFO] --- jar:3.3.0:jar (default-jar) @ sparkwebserver ---                                                                                                              
[INFO] Building jar: C:\Users\camar\OneDrive\Documentos\NetBeansProjects\SparkWebServer\target\sparkwebserver-1.0.jar
[INFO] 
[INFO] --- dependency:3.0.1:copy-dependencies (copy-dependencies) @ sparkwebserver ---                                                                                   
[INFO] Copying spark-core-2.9.2.jar to C:\Users\camar\OneDrive\Documentos\NetBeansProjects\SparkWebServer\target\dependency\spark-core-2.9.2.jar
[INFO] Copying jetty-server-9.4.30.v20200611.jar to C:\Users\camar\OneDrive\Documentos\NetBeansProjects\SparkWebServer\target\dependency\jetty-server-9.4.30.v20200611.jar
[INFO] Copying javax.servlet-api-3.1.0.jar to C:\Users\camar\OneDrive\Documentos\NetBeansProjects\SparkWebServer\target\dependency\javax.servlet-api-3.1.0.jar           
[INFO] Copying jetty-http-9.4.30.v20200611.jar to C:\Users\camar\OneDrive\Documentos\NetBeansProjects\SparkWebServer\target\dependency\jetty-http-9.4.30.v20200611.jar
[INFO] Copying jetty-util-9.4.30.v20200611.jar to C:\Users\camar\OneDrive\Documentos\NetBeansProjects\SparkWebServer\target\dependency\jetty-util-9.4.30.v20200611.jar   
[INFO] Copying jetty-io-9.4.30.v20200611.jar to C:\Users\camar\OneDrive\Documentos\NetBeansProjects\SparkWebServer\target\dependency\jetty-io-9.4.30.v20200611.jar       
[INFO] Copying jetty-webapp-9.4.30.v20200611.jar to C:\Users\camar\OneDrive\Documentos\NetBeansProjects\SparkWebServer\target\dependency\jetty-webapp-9.4.30.v20200611.jar
[INFO] Copying jetty-xml-9.4.30.v20200611.jar to C:\Users\camar\OneDrive\Documentos\NetBeansProjects\SparkWebServer\target\dependency\jetty-xml-9.4.30.v20200611.jar     
[INFO] Copying jetty-servlet-9.4.30.v20200611.jar to C:\Users\camar\OneDrive\Documentos\NetBeansProjects\SparkWebServer\target\dependency\jetty-servlet-9.4.30.v20200611.jar
[INFO] Copying jetty-security-9.4.30.v20200611.jar to C:\Users\camar\OneDrive\Documentos\NetBeansProjects\SparkWebServer\target\dependency\jetty-security-9.4.30.v20200611.jar
[INFO] Copying websocket-server-9.4.30.v20200611.jar to C:\Users\camar\OneDrive\Documentos\NetBeansProjects\SparkWebServer\target\dependency\websocket-server-9.4.30.v20200611.jar
[INFO] Copying websocket-common-9.4.30.v20200611.jar to C:\Users\camar\OneDrive\Documentos\NetBeansProjects\SparkWebServer\target\dependency\websocket-common-9.4.30.v20200611.jar
[INFO] Copying websocket-client-9.4.30.v20200611.jar to C:\Users\camar\OneDrive\Documentos\NetBeansProjects\SparkWebServer\target\dependency\websocket-client-9.4.30.v20200611.jar
[INFO] Copying jetty-client-9.4.30.v20200611.jar to C:\Users\camar\OneDrive\Documentos\NetBeansProjects\SparkWebServer\target\dependency\jetty-client-9.4.30.v20200611.jar
[INFO] Copying websocket-servlet-9.4.30.v20200611.jar to C:\Users\camar\OneDrive\Documentos\NetBeansProjects\SparkWebServer\target\dependency\websocket-servlet-9.4.30.v20200611.jar
[INFO] Copying websocket-api-9.4.30.v20200611.jar to C:\Users\camar\OneDrive\Documentos\NetBeansProjects\SparkWebServer\target\dependency\websocket-api-9.4.30.v20200611.jar
[INFO] Copying slf4j-api-1.7.25.jar to C:\Users\camar\OneDrive\Documentos\NetBeansProjects\SparkWebServer\target\dependency\slf4j-api-1.7.25.jar                         
[INFO] Copying slf4j-log4j12-1.7.25.jar to C:\Users\camar\OneDrive\Documentos\NetBeansProjects\SparkWebServer\target\dependency\slf4j-log4j12-1.7.25.jar                 
[INFO] Copying log4j-1.2.17.jar to C:\Users\camar\OneDrive\Documentos\NetBeansProjects\SparkWebServer\target\dependency\log4j-1.2.17.jar                                 
[INFO]                                                                                                                                                                   
[INFO] --- install:3.1.1:install (default-install) @ sparkwebserver ---                                                                                                  
[INFO] Installing C:\Users\camar\OneDrive\Documentos\NetBeansProjects\SparkWebServer\pom.xml to C:\Users\camar\.m2\repository\co\edu\escuelaing\sparkdockerdemolive\sparkwebserver\1.0\sparkwebserver-1.0.pom
[INFO] Installing C:\Users\camar\OneDrive\Documentos\NetBeansProjects\SparkWebServer\target\sparkwebserver-1.0.jar to C:\Users\camar\.m2\repository\co\edu\escuelaing\sparkdockerdemolive\sparkwebserver\1.0\sparkwebserver-1.0.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS                                                                                                                                                     
[INFO] ------------------------------------------------------------------------                                                                                          
[INFO] Total time:  3.850 s                                                                                                                                              
[INFO] Finished at: 2023-10-31T19:45:52-05:00                                                                                                                            
[INFO] ------------------------------------------------------------------------  
```
7. Asegúrese que las dependencias están en el directorio target y que continentemente las dependencia, es decir las librerías necesarias para correr en formato jar. En este caso solo son las dependencias necesarias para correr SparkJava.

![image](https://github.com/camrojass/SparkWebServer/assets/100396227/cc9b1df3-9b82-4ebf-9090-e53eb1de58e5)

8. Ejecute el programa invocando la máquina virtual de Java desde la línea de comandos y acceda la url http:localhost:4567/hello:
```
java -cp "target/classes;target/dependency/*" co.edu.escuelaing.sparkdockerdemolive.SparkWebServer
```
Debería ver algo así:
![image](https://github.com/camrojass/SparkWebServer/assets/100396227/6d371e7e-0837-4499-9b69-82feb60b9887)
**Nota:** En caso que se presente un error como este
```
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
```
Debe incluir en el archivo [pom.xml](https://github.com/camrojass/SparkWebServer/blob/main/pom.xml) la siguiente dependencia
```
        <!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-api -->
        <dependency>
              <groupId>org.slf4j</groupId>
              <artifactId>slf4j-api</artifactId>
              <version>1.7.25</version>
          </dependency>
          <dependency>
              <groupId>org.slf4j</groupId>
              <artifactId>slf4j-log4j12</artifactId>
              <version>1.7.25</version>
         </dependency>
```
y agregar el recursos log4j.proporties en la ruta \src\main\resources con la siguiente información (requiere volver a compilar)
```
# Root logger option
log4j.rootLogger=INFO, stdout

# Redirect log messages to console
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target=System.out
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n
```
## Segunda parte: crear imagen para docker y subirla
1. En la raíz de su proyecto cree un archivo denominado Dockerfile con el siguiente contenido:
 ```
FROM openjdk:8

WORKDIR /usrapp/bin

ENV PORT 6000

COPY /target/classes /usrapp/bin/classes
COPY /target/dependency /usrapp/bin/dependency

CMD ["java","-cp","./classes:./dependency/*","co.edu.escuelaing.sparkdockerdemolive.SparkWebServer"]
```
2. Usando la herramienta de línea de comandos de Docker construya la imagen:

```
docker build --tag dockersparkprimer .
```
3. Revise que la imagen fue construida
```
docker images
```
Debería ver algo así:
```
%> docker images
REPOSITORY          TAG       IMAGE ID       CREATED          SIZE
dockersparkprimer   latest    6555a533530f   25 seconds ago   530MB
```
4. A partir de la imagen creada cree tres instancias de un contenedor docker independiente de la consola (opción “-d”) y con el puerto 6000 enlazado a un puerto físico de su máquina (opción -p):
```
docker run -d -p 34000:6000 --name firstdockercontainer dockersparkprimer
docker run -d -p 34001:6000 --name firstdockercontainer2 dockersparkprimer
docker run -d -p 34002:6000 --name firstdockercontainer3 dockersparkprimer
```
5. Asegúrese que el contenedor está corriendo
```
docker ps
```
Debería ver algo así:
```
CONTAINER ID   IMAGE               COMMAND                  CREATED          STATUS          PORTS                     NAMES
1a57115ada14   dockersparkprimer   "java -cp ./classes:…"   6 seconds ago    Up 4 seconds    0.0.0.0:34002->6000/tcp   firstdockercontainer3
35f78f55724c   dockersparkprimer   "java -cp ./classes:…"   14 seconds ago   Up 13 seconds   0.0.0.0:34001->6000/tcp   firstdockercontainer2
94b8af768401   dockersparkprimer   "java -cp ./classes:…"   30 seconds ago   Up 28 seconds   0.0.0.0:34000->6000/tcp   firstdockercontainer
```

6. Acceda por el browser a http://localhost:34002/hello, o a http://localhost:34000/hello, o a http://localhost:34001/hello para verificar que están corriendo.

![image](https://github.com/camrojass/SparkWebServer/assets/100396227/f17cf0d5-d456-4476-b87c-e006a536c96e)
![image](https://github.com/camrojass/SparkWebServer/assets/100396227/2fa33e15-b589-4f84-84ef-6e0f5f92063e)
![image](https://github.com/camrojass/SparkWebServer/assets/100396227/7c8d6b24-91ed-41d8-83ab-7e90975cf55a)


8. Use docker-compose para generar automáticamente una configuración docker, por ejemplo un container y una instancia a de mongo en otro container. Cree en la raíz de su directorio el archivo docker-compose.yml con le siguiente contenido:

```
version: '2'


services:
    web:
        build:
            context: .
            dockerfile: Dockerfile
        container_name: web
        ports:
            - "8087:6000"
    db:
        image: mongo:3.6.1
        container_name: db
        volumes:
            - mongodb:/data/db
            - mongodb_config:/data/configdb
        ports:
            - 27017:27017
        command: mongod
        
volumes:
    mongodb:
    mongodb_config:
```

8. Ejecute el docker compose:

```
docker-compose up -d
```

9. Verifique que se crearon los servicios:
```
docker ps
```
Debería ver algo así desde la ventana de comandos
```
CONTAINER ID   IMAGE                COMMAND                  CREATED          STATUS          PORTS
 NAMES
01b76af4f694   sparkwebserver-web   "java -cp ./classes:…"   19 seconds ago   Up 12 seconds   0.0.0.0:8087->6000/tcp     web
b59866da617a   mongo:3.6.1          "docker-entrypoint.s…"   19 seconds ago   Up 12 seconds   0.0.0.0:27017->27017/tcp   db
1a57115ada14   dockersparkprimer    "java -cp ./classes:…"   9 minutes ago    Up 9 minutes    0.0.0.0:34002->6000/tcp    firstdockercontainer3
35f78f55724c   dockersparkprimer    "java -cp ./classes:…"   9 minutes ago    Up 9 minutes    0.0.0.0:34001->6000/tcp    firstdockercontainer2
94b8af768401   dockersparkprimer    "java -cp ./classes:…"   10 minutes ago   Up 10 minutes   0.0.0.0:34000->6000/tcp    firstdockercontainer
```
Debería ver algo así en el Docker Desktop dashboard:
![image](https://github.com/camrojass/SparkWebServer/assets/100396227/fcd5c6ca-d2f4-40ec-a028-57dcf002d08d)

## Tercera Parte: Subir imagen a Docker Hub
1. Ingrese a su cuenta de Dockerhub.
![image](https://github.com/camrojass/SparkWebServer/assets/100396227/bc9185f7-46e5-4231-bd7a-44169f565232)

2. Acceda al menú de repositorios y cree un repositorio
![image](https://github.com/camrojass/SparkWebServer/assets/100396227/f852b92e-9cda-4410-881a-83d632f98580)

3. En su motor de docker local cree una referencia a su imagen con el nombre del repositorio a donde desea subirla:
```
docker tag dockersparkprimer camrojass/first_spark_web_app
```
4. Verifique que la nueva referencia de imagen existe
```
docker images
```
Debería ver algo así
```
REPOSITORY                      TAG       IMAGE ID       CREATED          SIZE
camrojass/first_spark_web_app   latest    6555a533530f   16 minutes ago   530MB
dockersparkprimer               latest    6555a533530f   16 minutes ago   530MB
sparkwebserver-web              latest    dbe204c41187   16 minutes ago   530MB
mongo                           3.6.1     1200574c8af9   5 years ago      366MB
```
5. Autentíquese en su cuenta de dockerhub (ingrese su usuario y clave si es requerida):
```
docker login
```
6. Empuje (Push) la imagen al repositorio en DockerHub
```
docker push dnielben/firstsprkwebapprepo:latest
```
En la pantalla de Tags de su repositorio en Dockerhub debería ver algo así:
![image](https://github.com/camrojass/SparkWebServer/assets/100396227/84091183-8ba4-4cd4-9ba6-c27d79c0a161)
## Cuarta Parte: Subir imagen a Docker Hub
1. Cree una máquina virtual linux en AWS
2. Acceda a la máquina virtual
![image](https://github.com/camrojass/SparkWebServer/assets/100396227/eaa21d3a-95fe-4104-887c-a2b0e8eaed33)
3. Instale Docker
```
sudo yum update -y
```
```
sudo yum install docker
```
4. Inicie el servicio de docker
```
sudo service docker start
```
5. Configure su usuario en el grupo de docker para no tener que ingresar “sudo” cada vez que invoca un comando
```
sudo usermod -a -G docker ec2-user
```
6. Desconectese de la maquina virtual e ingrese nuevamente para que la configuración de los grupos de usuarios tenga efecto.

7. A partir de la imagen creada en Dockerhub cree una instancia de un contenedor docker independiente de la consola (opción “-d”) y con el puerto 6000 enlazado a un puerto físico de su máquina (opción -p):
```
docker run -d -p 42000:6000 --name firstdockerimageaws dnielben/firstsprkwebapprepo
```
Debería ver algo así a través de ```docker ps```
```
CONTAINER ID   IMAGE                           COMMAND                  CREATED             STATUS             PORTS                                         NAMES
f818dc1ce984   camrojass/first_spark_web_app   "java -cp ./classes:…"   About an hour ago   Up About an hour   0.0.0.0:42000->6000/tcp, :::42000->6000/tcp   firstdockerimageaws
```
8. Abra los puertos de entrada del security group de la máxima virtual para acceder al servicio
![image](https://github.com/camrojass/SparkWebServer/assets/100396227/36497c91-8f99-4c6d-84f6-25d96ca4a944)

9. Verifique que pueda acceder en una url similar a esta (la url específica depende de los valores de su maquina virtual EC2)

http://ec2-54-209-214-238.compute-1.amazonaws.com:42000/hello
![image](https://github.com/camrojass/SparkWebServer/assets/100396227/5218594b-e54a-48fa-8141-a28c56ff93ca)


## Autores ✒️
* **Luis Daniel Benavides** - *Versión inicial* - [dnielben](https://github.com/dnielben) 
* **Camilo Alejandro Rojas** - *Trabajo y documentación* - [camrojass](https://github.com/camrojass)
