/*
 * Copyright (c) 2014 iDENTITY AUTOMATiON, LP. All rights reserved.
 */

import jcifs.smb.SmbFile;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

import static java.net.Authenticator.RequestorType.SERVER;
import static javax.swing.text.html.HTML.Tag.I;

/**
 * @author Shon Vella (svella@idauto.net)
 */
public class JCIFSTest {

    private static final String PROPERTIES = "../../jcifs.properties";

    private static String SERVER;
    private static String SERVER_IP;
    private static String SHARE;
    @SuppressWarnings("FieldCanBeLocal")
    private static String DIR;

    private static final String[] requiredProperties = {"SERVER", "SERVER_IP", "SHARE", "DIR", "jcifs.smb.client.domain", "jcifs.smb.client.username", "jcifs.smb.client.password"};

    private static String WRITE_DIR;
    private static String SRC_DIR;
    private static String FILE1;
    private static String URL_SERVER;
    private static String URL_SHARE;
    private static String URL_WRITE_DIR;

    @BeforeClass
    void beforeClass() throws IOException {
        Properties properties = new Properties();
        InputStream propertiesInput = new FileInputStream(PROPERTIES);
        try {
            properties.load(propertiesInput);

            for (String prop : requiredProperties) {
                if (properties.getProperty(prop, "").isEmpty()) {
                    throw new IllegalStateException("Missing required property " + prop + " in " + PROPERTIES);
                }
            }
            SERVER = properties.getProperty("SERVER");
            SERVER_IP = properties.getProperty("SERVER_IP");
            SHARE = properties.getProperty("SHARE");
            DIR = properties.getProperty("DIR");

            WRITE_DIR = DIR + "/";
            SRC_DIR = DIR + "/Junk/";
            FILE1 = SRC_DIR + "10883563.doc";
            URL_SERVER = "smb://" + SERVER + "/";
            URL_SHARE = URL_SERVER + SHARE + "/";
            URL_WRITE_DIR = URL_SHARE + WRITE_DIR;

            for (Map.Entry<Object,Object> propEntry : properties.entrySet()) {
                System.setProperty((String)propEntry.getKey(), (String)propEntry.getValue());
            }

            SmbFile dir = new SmbFile(URL_WRITE_DIR);
            if (dir.exists()) {
                dir.delete();
            }
            SmbFile srcDir = new SmbFile(URL_SHARE + SRC_DIR);
            if (!srcDir.exists()) {
                srcDir.mkdirs();
            }
            SmbFile file1 = new SmbFile(URL_SHARE + FILE1);
            if (!file1.exists()) {
                file1.createNewFile();
                OutputStream os = file1.getOutputStream();
                try {
                    IOUtils.copy(getClass().getResourceAsStream("10883563.doc"), os);
                } finally {
                    IOUtils.closeQuietly(os);
                }
            }

        } finally {
            IOUtils.closeQuietly(propertiesInput);
        }
    }

    @AfterClass
    void afterClass() throws Exception {
    }

    @Test
    public void SidLookup() throws Exception {
        SidLookup.main(new String[]{SERVER, "S-1-5-21-2779991279-2625083122-3494051191-1361"});
    }

    @Test()
    public void TestGetParent() throws Exception {
        TestGetParent.main(new String[]{"smb://"});
        TestGetParent.main(new String[]{URL_WRITE_DIR});
    }

    @Test()
    public void TestListLoop() throws Exception {
        TestListLoop.main(new String[]{URL_SHARE, "2"});
    }

    @Test()
    public void TestCopy() throws Exception {
        TestCopy.main(new String[]{URL_SHARE + "xxx/", URL_SHARE + "deleteme/"});
    }

    @Test()
    public void ListFiles() throws Exception {
        ListFiles.main(new String[]{"smb://"});
        ListFiles.main(new String[]{"smb://" + SERVER_IP + "/" + SHARE + "/"});
        ListFiles.main(new String[]{URL_WRITE_DIR});
        ListFiles.main(new String[]{URL_SERVER});
    }

    @Test(dependsOnMethods = "ListFiles")
    public void ListACL() throws Exception {
        ListACL.main(new String[]{URL_WRITE_DIR});
    }

    @Test()
    public void LargeListFiles() throws Exception {
        LargeListFiles.main(new String[]{URL_WRITE_DIR});
    }

    @Test()
    public void CountPerms() throws Exception {
        CountPerms.main(new String[]{URL_WRITE_DIR, "100"});
    }

    @Test()
    public void AclCrawler() throws Exception {
        AclCrawler.main(new String[]{URL_WRITE_DIR, "100"});
    }

    @Test()
    public void SidCacheTest() throws Exception {
        SidCacheTest.main(new String[]{URL_WRITE_DIR});
    }

    @Test()
    public void GetSecurity() throws Exception {
        GetSecurity.main(new String[]{URL_WRITE_DIR});
        GetSecurity.main(new String[]{URL_SHARE});
    }

    @Test()
    public void GetShareSecurity() throws Exception {
        GetShareSecurity.main(new String[]{URL_WRITE_DIR});
    }

    @Test()
    public void SidCrawler() throws Exception {
        SidCrawler.main(new String[]{URL_WRITE_DIR, "5"});
    }

    @Test()
    public void GetGroupMemberSidsFromURL() throws Exception {
        GetGroupMemberSidsFromURL.main(new String[]{URL_WRITE_DIR});
    }

    @Test()
    public void InterruptTest() throws Exception {
        InterruptTest.main(new String[]{URL_SHARE + FILE1});
    }

    @Test()
    public void AllocInfo() throws Exception {
        AllocInfo.main(new String[]{URL_SHARE});
    }

    @Test()
    public void Append() throws Exception {
        Append.main(new String[]{URL_WRITE_DIR + "Append.txt"});
    }

    // disabled by default because it requires interactive console input
    @Test(enabled = false)
    public void AuthListFiles() throws Exception {
        AuthListFiles.main(new String[]{"smb://bogus@" + SERVER + "/" + SHARE + "/"});
    }

    @Test()
    public void CopyTo() throws Exception {
        CopyTo.main(new String[]{URL_SHARE + SRC_DIR, URL_SHARE + WRITE_DIR + "CopyTo/"});
    }

    @Test()
    public void CreateFile() throws Exception {
        CreateFile.main(new String[]{URL_WRITE_DIR + "CreateFile.txt"});
    }

    @Test(dependsOnMethods = "CreateFile")
    public void Delete() throws Exception {
        Delete.main(new String[]{URL_WRITE_DIR + "CreateFile.txt"});
    }

    @Test()
    public void Equals() throws Exception {
        Equals.main(new String[]{URL_WRITE_DIR + "CreateFile.txt", URL_SHARE + WRITE_DIR + "../" + WRITE_DIR + "CreateFile.txt"});
    }

    @Test()
    public void Exists() throws Exception {
        Exists.main(new String[]{URL_WRITE_DIR});
    }

    @Test()
    public void FileInfo() throws Exception {
        FileInfo.main(new String[]{URL_SHARE + FILE1, "0"});
    }

    @Test()
    public void FileOps() throws Exception {
        FileOps.main(new String[]{URL_WRITE_DIR});
    }

    @Test()
    public void FilterFiles() throws Exception {
        FilterFiles.main(new String[]{URL_SHARE + SRC_DIR});
    }

    @Test()
    public void GetDate() throws Exception {
        GetDate.main(new String[]{URL_SHARE + FILE1});
    }

    @Test()
    public void Get() throws Exception {
        Get.main(new String[]{URL_SHARE + FILE1});
    }

    @Test()
    public void GetType() throws Exception {
        GetType.main(new String[]{URL_SHARE});
    }

    @Test()
    public void GrowWrite() throws Exception {
        GrowWrite.main(new String[]{URL_WRITE_DIR + "GrowWrite.txt"});
    }

    @Test(dependsOnMethods = "Append")
    public void GetURL() throws Exception {
        GetURL.main(new String[]{URL_WRITE_DIR + "Append.txt"});
    }

    @Test(dependsOnMethods = "Append")
    public void HttpURL() throws Exception {
        HttpURL.main(new String[]{URL_WRITE_DIR, "../Append.txt"});
    }

    @Test(dependsOnMethods = "HttpURL")
    public void Interleave() throws Exception {
        Interleave.main(new String[]{URL_WRITE_DIR, "3"});
    }

    @Test()
    public void IsDir() throws Exception {
        IsDir.main(new String[]{URL_SHARE + SRC_DIR});
    }

    @Test()
    public void Length() throws Exception {
        Length.main(new String[]{URL_SHARE + FILE1});
    }

    @Test()
    public void List() throws Exception {
        List.main(new String[]{URL_WRITE_DIR});
    }

    @Test()
    public void ListTypes() throws Exception {
        ListTypes.main(new String[]{URL_WRITE_DIR});
    }

    @Test()
    public void Mkdir() throws Exception {
        Mkdir.main(new String[]{URL_WRITE_DIR + "Mkdir"});
    }

    // disabled by default because it depends on things that aren't available in my (svella) environment
    @Test(enabled = false)
    public void NodeStatus() throws Exception {
        NodeStatus.main(new String[]{SERVER});
    }

    @Test()
    public void Put() throws Exception {
        Put.main(new String[]{URL_WRITE_DIR + "Makefile"});
    }

    @Test()
    public void Query() throws Exception {
        Query.main(new String[]{SERVER});
    }

    @Test(dependsOnMethods = "Put")
    public void RenameTo() throws Exception {
        RenameTo.main(new String[]{URL_WRITE_DIR + "Makefile", URL_WRITE_DIR + "Makefile.txt"});
    }

    @Test(dependsOnMethods = "RenameTo")
    public void SetAttrs() throws Exception {
        SetAttrs.main(new String[]{URL_WRITE_DIR + "Makefile.txt", "FFFF"});
    }

    @Test(dependsOnMethods = "SetAttrs")
    public void SetTime() throws Exception {
        SetTime.main(new String[]{URL_WRITE_DIR + "Makefile.txt"});
    }

    @Test()
    public void SlowWrite() throws Exception {
        SlowWrite.main(new String[]{URL_WRITE_DIR + "SlowWrite.txt"});
    }

    @Test(dependsOnMethods = "SlowWrite")
    public void SlowRead() throws Exception {
        SlowRead.main(new String[]{URL_WRITE_DIR + "SlowWrite.txt"});
    }

    @Test()
    public void SmbCrawler() throws Exception {
        SmbCrawler.main(new String[]{URL_WRITE_DIR, "1000"});
    }

    @Test()
    public void T2Crawler() throws Exception {
        T2Crawler.main(new String[]{URL_WRITE_DIR, "3", "1000"});
    }

    @Test()
    public void TestRandomAccess() throws Exception {
        TestRandomAccess.main(new String[]{URL_WRITE_DIR + "TestRandomAccess.bin", "1"});
        TestRandomAccess.main(new String[]{URL_WRITE_DIR + "TestRandomAccess.bin", "2", "0"});
        TestRandomAccess.main(new String[]{URL_WRITE_DIR + "TestRandomAccess.bin", "3", "1234"});
    }

}
