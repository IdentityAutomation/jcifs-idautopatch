# jcifs-idautopatch
Patched version of JCIFS Library - http://jcifs.samba.org/


Maven artifact
```xml
<dependency>
  <groupId>net.idauto.oss.jcifs</groupId>
  <artifactId>jcifs-idautopatch</artifactId>
  <version>1.3.18.8</version>
</dependency>    
```    

or download jar from [Releases](https://github.com/IdentityAutomation/jcifs-idautopatch/releases)

Patches include:

* Bug fixes and minor changes from [Identity Automation](http://www.identityautomation.com)
  * Support for dynamic response timeouts via SmbFile.setReadTimeout() when reading from a Named Pipe.
  * [WriterThreads are leaking](https://lists.samba.org/archive/jcifs/2013-October/010115.html)
  * [Issues with closing destination file in SmbFile.copyTo()](https://lists.samba.org/archive/jcifs/2014-June/010165.html) 
  * Change default value for jcifs property jcifs.smb.client.ignoreCopyToException from true to false
  * Don't [logoff after failed SmbComTreeConnectAndX](https://lists.samba.org/archive/jcifs/2013-December/010121.html)
  * [Patch for lsa and samr handle leaks](https://lists.samba.org/archive/jcifs/2014-September/010179.html)
  * Add property jcifs.smb.client.port139.enabled and default it to false to address [java.io.IOException: Failed to establish session](https://lists.samba.org/archive/jcifs/2015-June/010292.html)
  
* [Seven jCIFS bug fixes from Google](https://lists.samba.org/archive/jcifs/2014-September/010177.html) 
  * Originally hosted on googlecode, but currently residing at https://github.com/googlegsa/filesystem.v3/tree/master/projects/jcifs
* Changes by Chris Dail to
[support setting share permissions](http://chrisdail.com/2012/03/15/hacking-jcifs-set-permissions/).
  * Originally hosted at https://github.com/chrisdail/jcifs


