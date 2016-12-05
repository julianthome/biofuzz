# BioFuzz - A Crawljax plugin for testing Web applications

BioFuzz is a security testing tool for finding security vulnerabilities
in Web application and/or Web services; the details of the approach can be
found [here](http://www.specmate.org/papers/2014-06-Search-BasedSecurityTestingofWebApplications.pdf).

# Installation

A ready-to-use VirtualBox image of BioFuzz together with WebChess (one of the test subjects) is available [here](https://dropit.uni.lu/invitations?share=5652547adaabff7db807&dl=1). The login for authentication is user `biofuzz` with password `biofuzz`. [This screencast](https://dropit.uni.lu/invitations?share=babfb75907a530b2606b&dl=1) may help to get an idea of how BioFuzz can be used. The remainder of this documentation illustrates the local installation of BioFuzz.

## BioFuzz toolkit Installation

BioFuzz relies on the BioFuzz toolkit which is used for input generation and
input evolution. The following steps illustrate how to compile the BioFuzz
toolkit:

```bash
git clone https://github.com/julianthome/biofuzz-tk.git
cd biofuzz-tk/
mvn package
```
After those steps are completed, the file `target/biofuzz-tk-Alpha.jar` should be available.

## Compiling BioFuzz

BioFuzz itself is a [Crawljax](http://crawljax.com/) plugin and depends on [WebScarab](https://www.owasp.org/index.php/Category:OWASP_WebScarab_Project) which is a HTTP proxy that captures all of the requests that are being exchanged between the Web client and the Web server. Unfortunately, WebScarab is not available on Maven central, but the jar file may be downloaded [here](https://dropit.uni.lu/invitations?share=724dad6076fae28cef15&dl=1).

To compile BioFuzz, the following commands have to be executed:

```bash
git clone https://github.com/julianthome/biofuzz.git
cd biofuzz
```

After that, the absolute paths of WebScarab and the BioFuzz toolkit have to be set within the `pom.xml` file; then BioFuzz can be compiled by invoking `mvn compile`.

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
For web browser automation, BioFuzz requires the geckodriver. The latest
version can be downloaded [here](https://github.com/mozilla/geckodriver/releases).
Unfortunately, the location of the geckodriver has to be set manually by
adding `System.setProperty("webdriver.gecko.driver”, <path to geckodriver>);` to the source code to point to the appropriate location.

Afterwards, one should be able to execute `TestBioFuzzContentHandler`.
The test case will automatically authenticate a user `webchess` with his
password `webchess`.


## Test subject Installation

The WebChess test subject can be downloaded [here](https://dropit.uni.lu/invitations?share=012ccd31f72b9176d8c2&dl=1) and its installation is explained in the following.

To run WebChess, first the database has to be created:

```sql
CREATE DATABASE webchess;
GRANT ALL PRIVILEGES ON webchess.* TO 'webchess'@'localhost' IDENTIFIED BY 'webchess';
```

After downloading WebChess [here](https://dropit.uni.lu/invitations?share=012ccd31f72b9176d8c2&dl=1), it may be installed by executing the following commands:

```bash
unzip WebChess_1.0.0rc2.zip
mv webchess/ /var/www/
chown -R www-data:www-data /var/www/webchess/
```

WebChess can be installed by opening the URL `http://localhost/webchess/install.php` in a browser and by following the installation procedure.

## Database logger

BioFuzz requires access to the database queries that are being
exchanged between the Web server and the DBMS. For this purpose we
have instrumented the `php_mysql` module. The steps necessary to get
access to the database statements are illustrated in the following.
The explanations are based on Ubuntu 12.02. Note that BioFuzz expects the
database queries to be logged to `/tmp/logger.log` and does not depend on any specific logger.

To get the sources, and to prepare the built of the php MySQL module,
one may execute the following commands:

```bash
apt-get source php5_mysql
tar xvf php5_5.3.10.orig.tar.gz
cd php-5.3.10
./configure --with-config-file-path=/etc/php5/apache2 --with-config-file-scan-dir=/etc/php5/apache2/conf.d --sysconfdir=/etc --localstatedir=/var --mandir=/usr/share/man --disable-debug --with-regex=php --disable-rpath --disable-static --with-pic --with-layout=GNU --with-pear=/usr/share/php --enable-sysvsem --enable-sysvshm --enable-sysvmsg --enable-bcmath --with-bz2 --enable-ctype --without-gdbm --with-iconv --with-gettext --enable-mbstring --enable-shmop --enable-sockets --enable-wddx --with-libxml-dir=/usr --with-zlib --with-kerberos=/usr --with-openssl --enable-zip --with-mhash=yes --with-exec-dir=/usr/lib/php5/libexec --with-mysql-sock=/var/run/mysqld/mysqld.sock --with-mysql=shared,/usr --with-pdo-mysql=shared,/usr --with-mysqli=shared,/usr/bin/mysql_config --with-libdir=/lib/i386-linux-gnu/
```

Afterwards, the file `php_mysql.c` has to be changed such that all the database
queries are logged to `/tmp/logger.log`. To get an idea on how to modify the module, please take a look the the patch file which can be downloaded [here](https://dropit.uni.lu/invitations?share=2484bc72ffefa0068c6e&dl=1).

After running `make`, you should be able to see the file `mysql.so` in
the `modules/` subdirectory. On an Ubuntu system this file can be
copied to `/usr/lib/php5/20090626+lfs`. After restarting apache the module is up and ready.
