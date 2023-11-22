package com.tzi.lab.encryption;

import com.tzi.lab.entities.KeyGenOut;
import com.tzi.lab.services.encrypt.EncryptionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.tzi_lib.MD5Hash;
import org.tzi_lib.RC5CBCPad;
import org.tzi_lib.RSA;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.tzi_lib.ByteConverter.hexStringToBytes;

@SpringBootTest
class EncryptionServiceTests {

    @Mock
    private MD5Hash md5Hash;

    @Mock
    private RC5CBCPad rc5CBCPad;

    @Mock
    private RSA rsa;

    @InjectMocks
    private EncryptionServiceImpl encryptionService;

    @BeforeEach
    void setUp() {
        org.mockito.Mockito.reset(md5Hash, rc5CBCPad, rsa);
    }

    @Test
    void generateKeys() throws Exception {
        KeyPair keyPair = new KeyPair(MockUtils.generateMockPublicKey(), MockUtils.generateMockPrivateKey());
        when(rsa.generateKeyPair()).thenReturn(keyPair);

        KeyGenOut keys = encryptionService.generateKeys();

        assertEquals(MockUtils.publicKeyHex, keys.getPublicKey());
        assertEquals(MockUtils.privateKeyHex, keys.getPrivateKey());
    }

    @Test
    void encryptRC5() {
        byte[] input = "Test Input".getBytes();
        int iv = 123;
        String key = "TestKey";
        String expectedEncrypted = "TestEncrypted";

        when(md5Hash.computeMD5(any())).thenReturn("MockedMD5Hash".getBytes());
        when(rc5CBCPad.encryptBlocks(any(), any(), any())).thenReturn(expectedEncrypted);

        String encrypted = encryptionService.encryptRC5(input, iv, key);

        assertEquals(expectedEncrypted, encrypted);
    }

    @Test
    void encryptRSA() throws Exception {
        byte[] input = "Test Input".getBytes();
        String publicKeyHex = "MockedPublicKeyHex";
        String expectedEncrypted = "TestEncrypted";

        when(rsa.encryptBlocks(any(), any())).thenReturn(expectedEncrypted);

        String encrypted = encryptionService.encryptRSA(input, publicKeyHex);

        assertEquals(expectedEncrypted, encrypted);
    }

    @Test
    void decryptRC5() {
        byte[] input = "9D4269052EFB7E5625DB4553FAFE253C".getBytes();
        String key = "encryptiontest";
        int iv = 123;
        String expectedDecrypted = "TestDecrypted";

        when(md5Hash.computeMD5(any())).thenReturn("9BD9F45C7959AA4EB27D3843D6DA355A".getBytes());
        when(rc5CBCPad.decryptBlocks(any(), any(), any())).thenReturn(expectedDecrypted);
        String decrypted = encryptionService.decryptRC5(input, key, iv);

        assertEquals(expectedDecrypted, decrypted);
    }

    @Test
    void decryptRSA() throws Exception {
        byte[] input = "Test Input".getBytes();
        String privateKeyHex = MockUtils.privateKeyHex;
        String expectedDecrypted = "TestDecrypted";

        when(rsa.decryptBlocks(any(), any())).thenReturn(expectedDecrypted);

        String decrypted = encryptionService.decryptRSA(input, privateKeyHex);

        assertEquals(expectedDecrypted, decrypted);
    }

    static class MockUtils {
        static String publicKeyHex = "30820122300D06092A864886F70D01010105000382010F003082010A0282010100BBF85746" +
                "8508EEEA7E01CA648D6BF6BABC5B111441E329D72701ABC8746EB3FB7794FFEDE72BEE19771C0F1A581D7627FBBDCD267D" +
                "FF0B9D81DE90A1A9CBEF8841406B443AEC74DEC6FA931CF03897E9B2B7631161E720432B6C37E2D14DCBDEDB9F26322427A" +
                "0A950243D29097E68BA9C7B987644BEF5A79D319A50693F61D841D10641D0523A605A4C60A4A6C8D9CE9D759A43AC72858" +
                "3FACE074C1DC517419BDD54E353630EBF017B58C6953A6F449BBE9CB9F315C30054818D45302178DD964049667461639469" +
                "6470988F0FE96B70B66F3254D9DBFD7C8CE406C1AA7A74C6EF4C0D77A141A5CCEC6DD3EAFD3AD180851355891CA508F3CB7" +
                "3EA55E24C230203010001";
        static String privateKeyHex = "308204BD020100300D06092A864886F70D0101010500048204A7308204A302010002820101" +
                "00BBF857468508EEEA7E01CA648D6BF6BABC5B111441E329D72701ABC8746EB3FB7794FFEDE72BEE19771C0F1A581D762" +
                "7FBBDCD267DFF0B9D81DE90A1A9CBEF8841406B443AEC74DEC6FA931CF03897E9B2B7631161E720432B6C37E2D14DCBDED" +
                "B9F26322427A0A950243D29097E68BA9C7B987644BEF5A79D319A50693F61D841D10641D0523A605A4C60A4A6C8D9CE9D759" +
                "A43AC728583FACE074C1DC517419BDD54E353630EBF017B58C6953A6F449BBE9CB9F315C30054818D45302178DD964049667" +
                "4616394696470988F0FE96B70B66F3254D9DBFD7C8CE406C1AA7A74C6EF4C0D77A141A5CCEC6DD3EAFD3AD180851355891C" +
                "A508F3CB73EA55E24C23020301000102820100256CD22FABC036158126291E711BEC618CAC130FF5F85D35C5C1EAEA7BE82" +
                "8B4AA4003DEE1B698EF000C99A334A51507EBF303DC249CF9BA900778C37F53A7EAF896F3647FD8E182139B035C7125CB7" +
                "54FCC7C9092EE1A39D574F401988BC54A276F1E0340B8951FF0E16E3DF3F273B6A7FE61C23678D0DF610865C199DAF1476DD" +
                "9761F3DAC2443E118A50C1E83FEDDA2DAC6EB6797B0A625CB116EAEC5B710575970B9CFF3CBE4CA724268B01D718D2FC9D4F" +
                "71E1BD11BF6260EB1972FFA7868ECC60A6BC8E192B10251B2F219DEE0AA7F35E491005F184DE30C649FDB160816BD4407E" +
                "AB3E660F88992263818B6515A7B4203FA2E93A5610E9380D04E449502818100FB07820D81DF2EE2C2F5383362DA3FA5CC8" +
                "0462953F70E8F8014D7DF3F57001212175930C14FC80A1385B910533784A43933E5CE4C5BA894DAA5591F711ACE100FDCD62" +
                "F5F85813D649BD25BA46FFE8715D5AE1010187D211BB280552659129DB90094A5E721126ECB6A397A215479B93E314D6513" +
                "B143369E9F8483F9C3352F02818100BFB12DF1E657C689EF2F3FF86B91F90DF40F012B5AEC4A195D4CDF9E9E71BE313A24C8" +
                "018B39E13C284BC12EE5335A534B75A882E23E2A3CB7F6CF708BE0AE76C7F63625982EEF447A1D959C335DBB9E796359E98A" +
                "BF6D2661C9B2A845DF642FFE0B3A3DA7483824E3A8458F9196D0748AA19CB1FF5236D3C2CB56DA84C0434D02818100AF8C6" +
                "D979296084CCE37FCC87ABDD3BAD7CB70C56011AADE119D01E5FE7E91EF1135C0CAC6B54B583894D079A9286F1B0561D4" +
                "B8B34F7A1236E160F50AFCCAD412B3C61934FCC107B4FEFF59D521F8E6710F907D6D3422F56F50224CE9D76A9C600F16" +
                "E4FA40778A81B8545FA34557268366A91021785A5FAD0832FCB4429E430281801E4EEB4EC5F6B6E37437EA4B62E6AF942" +
                "F347820DCBD6371E338965AC3FE83EDD09DA429EA537A734283C7C59C63A4326B49ED1CD49CB39CEA1151F71F86A4CF0F0" +
                "AB4C0F8F0E2CFD6E6FA92B9E46617C66598FFBDFA4A4DB94464C50A5729C614CABDA8A5DC5B5429D29F3CF5EDFC8390AFC0" +
                "D0BBE7C9D76D832CF65BEAF239028180554B503C92C72F43B45A9FC2639961230487A3A42E4054580B659EEB80C6BA9BA1E" +
                "E6E060550E953C2A5C6244058ED652BDA32A85D3BFC3A622E32EC184FE854D2D828B801B2EC87757A5EA0DCCBE3F3D5B1" +
                "EAB9C519A98708FE36CB0DA0367370CA754993DF7C62A21B3BAC5EDB5B609DA4492557AE7C4664637ED83E4403AB";

        static PublicKey generateMockPublicKey() throws Exception {

            byte[] keyBytes = hexStringToBytes(publicKeyHex);
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
        }

        static PrivateKey generateMockPrivateKey() throws Exception {
            byte[] keyBytes = hexStringToBytes(privateKeyHex);
            return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        }
    }
}
