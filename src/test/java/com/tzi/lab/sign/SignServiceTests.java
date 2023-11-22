package com.tzi.lab.sign;

import com.tzi.lab.entities.KeyGenOut;
import com.tzi.lab.entities.Signature;
import com.tzi.lab.services.sign.SignServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.tzi_lib.DSA;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.tzi_lib.ByteConverter.bytesToHex;
import static org.tzi_lib.ByteConverter.hexStringToBytes;

@SpringBootTest
class SignServiceTests {

    @Mock
    private DSA dsa;

    @InjectMocks
    private SignServiceImpl signService;

    @BeforeEach
    void setUp() {
        org.mockito.Mockito.reset(dsa);
    }

    @Test
    void generateKeys() throws Exception {
        KeyPair keyPair = new KeyPair(MockUtils.generateMockPublicKey(), MockUtils.generateMockPrivateKey());
        when(dsa.generateDSAKeyPair()).thenReturn(keyPair);

        KeyGenOut keys = signService.generateKeys();

        assertNotNull(keys);
    }

    @Test
    void sign() throws Exception {
        byte[] input = "Test Input".getBytes();
        String key = MockUtils.generateMockPrivateKeyString();
        String expectedSignature = "TestSignature";

        when(dsa.sign(any(), any())).thenReturn(expectedSignature);

        Signature signature = signService.sign(input, key);

        assertNotNull(signature);
        assertEquals(expectedSignature, signature.getSignature());
    }

    @Test
    void verifySuccess() throws Exception {
        byte[] input = "Test Input".getBytes();
        String signature = "4AB4C69BC867F23279BD873F5B1A94D8231FB5250D845B859A467EFEBC4A8F33BDD72F947BC340C5";
        String key = MockUtils.generateMockPublicKeyString();

        when(dsa.verify(any(), any(), any())).thenReturn(true);

        boolean verified = signService.verify(input, signature, key);
        assertTrue(verified);
    }

    @Test
    void verifyFailure() throws Exception {
        byte[] input = "Test Input".getBytes();
        String signature = "4AB4C69BC867F23279BD873F5B1A94D8231FB5250D845B859A467EFEBC4A8F33BDD72F947BC340C5";
        String key = MockUtils.generateMockPublicKeyString();

        when(dsa.verify(any(), any(), any())).thenReturn(false);

        boolean verified = signService.verify(input, signature, key);
        assertFalse(verified);
    }

    static class MockUtils {

        static PublicKey generateMockPublicKey() throws Exception {
            String publicKeyString = "308201B73082012C06072A8648CE3804013082011F02818100FD7F53811D75122952DF4A9C2EECE4" +
                    "E7F611B7523CEF4400C31E3F80B6512669455D402251FB593D8D58FABFC5F5BA30F6CB9B556CD7813B801D346FF26660B7" +
                    "6B9950A5A49F9FE8047B1022C24FBBA9D7FEB7C61BF83B57E7C6A8A6150F04FB83F6D3C51EC3023554135A16913" +
                    "2F675F3AE2B61D72AEFF22203199DD14801C70215009760508F15230BCCB292B982A2EB840BF0581CF502818100F7" +
                    "E1A085D69B3DDECBBCAB5C36B857B97994AFBBFA3AEA82F9574C0B3D0782675159578EBAD4594FE67107108180B4" +
                    "49167123E84C281613B7CF09328CC8A6E13C167A8B547C8D28E0A3AE1E2BB3A675916EA37F0BFA213562F1FB627A01243" +
                    "BCCA4F1BEA8519089A883DFE15AE59F06928B665E807B552564014C3BFECF492A0381840002818027A512C65BECF54B" +
                    "584DA54FD8C3693A1D35BD2FB62118F1A62748F21F4A36584F46196E6596B6D41046D4F40BD4D8FC92AF001D55FCE" +
                    "8B73ED41AB653E2F97DB535402C134B25113DD9B758DD9D229A7708CE67FADF580E6F97BB410E13A46E8931A60221" +
                    "0A37C75B376EDF3B953A68FB671ED630C1BBFD521CC1BFA705DF8C";

            byte[] keyBytes = hexStringToBytes(publicKeyString);
            return KeyFactory.getInstance("DSA").generatePublic(new X509EncodedKeySpec(keyBytes));
        }

        static PrivateKey generateMockPrivateKey() throws Exception {
            String privateKeyString = "3082014B0201003082012C06072A8648CE3804013082011F02818100FD7F53811D75122952" +
                    "DF4A9C2EECE4E7F611B7523CEF4400C31E3F80B6512669455D402251FB593D8D58FABFC5F5BA30F6CB9B556CD78" +
                    "13B801D346FF26660B76B9950A5A49F9FE8047B1022C24FBBA9D7FEB7C61BF83B57E7C6A8A6150F04FB83F6D3C51E" +
                    "C3023554135A169132F675F3AE2B61D72AEFF22203199DD14801C70215009760508F15230BCCB292B982A2EB840BF" +
                    "0581CF502818100F7E1A085D69B3DDECBBCAB5C36B857B97994AFBBFA3AEA82F9574C0B3D0782675159578EBAD4594" +
                    "FE67107108180B449167123E84C281613B7CF09328CC8A6E13C167A8B547C8D28E0A3AE1E2BB3A675916EA37F0BF" +
                    "A213562F1FB627A01243BCCA4F1BEA8519089A883DFE15AE59F06928B665E807B552564014C3BFECF492A04160214" +
                    "0AD09A284F79FE33CF5FA471AD6FD1F07CB5F914";

            byte[] keyBytes = hexStringToBytes(privateKeyString);
            return KeyFactory.getInstance("DSA").generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        }

        static String generateMockPrivateKeyString() throws Exception {
            PrivateKey privateKey = generateMockPrivateKey();
            return bytesToHex(privateKey.getEncoded());
        }

        static String generateMockPublicKeyString() throws Exception {
            PublicKey publicKey = generateMockPublicKey();
            return bytesToHex(publicKey.getEncoded());
        }
    }
}
