# BioFuzz - A Crawljax plugin for testing Web applications

# Installation

## BioFuzz Toolkit Installation

BioFuzz relies on the BioFuzz Toolkit which is used for input generation and
input evolution. The following steps illustrate how to compile the BioFuzz
Toolkit.

```bash
git clone https://github.com/julianthome/biofuzz-tk.git
cd biofuzz-tk/
mvn package
```
After those steps, the file `target/biofuzz-tk-Alpha.jar` should be available.

## Compiling BioFuzz

BioFuzz itself is a [Crawljax](http://crawljax.com/) plugin and depends on [WebScarab](https://www.owasp.org/index.php/Category:OWASP_WebScarab_Project) which is a HTTP proxy that captures all of the requests that are being exchanged between the web client and the web server. Unfortunately, WebScarab is not available through maven central, but one could download the jar
file from [here](https://dropit.uni.lu/invitations?share=724dad6076fae28cef15&dl=0).

For compiling biofuzz, please perform the following steps:

```bash
git clone https://github.com/julianthome/biofuzz.git
cd biofuzz.git
```

After that please modifiy the `pom.xml` file by filling-in the absolute paths of the webscarab and biofuzz-tk jar files.

```xml
<dependency>
  <groupId>org.biofuzztk</groupId>
  <artifactId>biofuzz-tk</artifactId>
  <version>Alpha</version>
  <scope>system</scope>
  <!-- set path here -->
  <systemPath>[path 1]/biofuzz-tk-Alpha.jar</systemPath>
</dependency>
<dependency>
  <groupId>org.owasp.webscarab</groupId>
  <artifactId>WebScarab</artifactId>
  <version>20070504-1631</version>
  <scope>system</scope>
  <!-- set path here -->
  <systemPath>[path 2]/webscarab-selfcontained-20070504-1631.jar</systemPath>
</dependency>
```

## Gekko
Depending on the platform, BioFuzz might require the geckodriver,
a web browser automation driver, to be installed. One could get the latest
version from ([here](https://github.com/mozilla/geckodriver/releases)).
Afterwards, one could run the `TestBioFuzzContentHandler` JUnit test case.
Unfortunately, the location of the geckodriver has to be set manually—In
TestBioFuzzContentHandler, one could change the line  
`System.setProperty("webdriver.gecko.driver”, <path to geckodriver>);`  to
point to the appropriate location. Furthermore, you might want to adjust the
URL for the web application under test as follows:

```java
BioFuzzBrowserMgr bmgr = new BioFuzzBrowserMgr(<webchess url here>, proxy)
BioFuzzFieldInput i0 = new BioFuzzFieldInput(BioFuzzAction.GO_TO_URL, < webchess url here>);
```

Afterwards, one should be able to execute `TestBioFuzzContentHandler`.
The test case will automatically authenticate a user `webchess` with his
password `webchess`.


## Test subject Installation

The WebChess test subject can be downloaded from [here](https://dropit.uni.lu/invitations?share=012ccd31f72b9176d8c2&dl=0) and its installation is explained in the following.

For running WebChess, you need to setup a database first:

```sql
CREATE DATABASE webchess;
GRANT ALL PRIVILEGES ON webchess.* TO 'webchess'@'localhost' IDENTIFIED BY 'webchess';
```

After downloading WebChess from [here](https://dropit.uni.lu/invitations?share=012ccd31f72b9176d8c2&dl=0), you could install it with the following commands:

```bash
unzip WebChess_1.0.0rc2.zip
mv webchess/ /var/www/
chown -R www-data:www-data /var/www/webchess/
```

You can install WebChess by opening a Web browser and going the the URL `http://localhost/webchess/install` and follow the installation procedure. After
the installation procedure you should be able to see the following login screen
after opening the URL `http://localhost/webchess`.

![](https://dropit.uni.lu/invitations?share=36728949612fe0a95a08&dl=1)


## Database logger

BioFuzz relies on access to the database access that are being
exchanged between the Web server and the DBMS. For this purpose, we
have instrumented the `php_mysql` module. The steps necessary to get
access to the database statements are illustrated in the following.
The explanations are based on Ubuntu 12.02. Note that BioFuzz expects the
database queries to be logged to `/tmp/logger.log` and does not care about
the logger that is being used.


For getting the sources, and for preparing the built of the php MySQL module,
one could execute the following commands:

```bash
apt-get source php5_mysql
tar xvf php5_5.3.10.orig.tar.gz
./configure --with-config-file-path=/etc/php5/apache2 --with-config-file-scan-dir=/etc/php5/apache2/conf.d --sysconfdir=/etc --localstatedir=/var --mandir=/usr/share/man --disable-debug --with-regex=php --disable-rpath --disable-static --with-pic --with-layout=GNU --with-pear=/usr/share/php --enable-sysvsem --enable-sysvshm --enable-sysvmsg --enable-bcmath --with-bz2 --enable-ctype --without-gdbm --with-iconv --with-gettext --enable-mbstring --enable-shmop --enable-sockets --enable-wddx --with-libxml-dir=/usr --with-zlib --with-kerberos=/usr --with-openssl --enable-zip --with-mhash=yes --with-exec-dir=/usr/lib/php5/libexec --with-mysql-sock=/var/run/mysqld/mysqld.sock --with-mysql=shared,/usr --with-pdo-mysql=shared,/usr --with-mysqli=shared,/usr/bin/mysql_config --with-libdir=/lib/i386-linux-gnu/
```

Afterwards, the file `php_mysql.c` has to be changed such that all the database
queries are logged to `/tmp/logger.log`. For getting an idea how to modify the module, please have a look the the patch file which can be downloaded from [here](https://dropit.uni.lu/invitations?share=2484bc72ffefa0068c6e&dl=0).

After running `make`, you should be able to see the file `mysql.so` in
the directory `php-5.3.10/modules/`. On an Ubuntu system this file can be
copied to `/usr/lib/php5/20090626+lfs`.

## Running BioFuzz

After executing all the above-mentioned steps, you should be able to execute
BioFuzz now.
