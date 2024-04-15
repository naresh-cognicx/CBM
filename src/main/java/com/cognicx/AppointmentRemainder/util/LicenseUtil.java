package com.cognicx.AppointmentRemainder.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class LicenseUtil {
    private static final Logger logger = LoggerFactory.getLogger(LicenseUtil.class);
    private PrivateKey privateKey;
    private PublicKey publicKey;

    private static final String PRIVATE_KEY_STRING = "MIILdAIBADANBgkqhkiG9w0BAQEFAASCC14wggtaAgEAAoICfgCFXbi4zQqYuM/OIOmoxmHVHW22CMoKwDX5g4m1dChOUO/94vgn4zxiGf+v6KTQnilk6kfSowJvZT4ixzcG3Zxc051CivxPrKyT+lxj2A3WkIxNG7Jg/rZNn7MLbguqUsoJarx30jY6Kx7WSmuFjklunNAtaTiMRfKGd9ICn9cBBOMAwXlpwW+uZh5uQPVIAoYKRTC9CJHAwg17hW2zW8eroGSptGGq1Cwx6U++zi3MUJuBuKbcQYgFdF922UtxClf6zhKen+F5rlTfEpxgugjHVZhHeaybYG6SAw5On+AO7UWjb0Fwc7FjBKc1351j4uvNHD7cNwRcXrT0IvktqVygZLzQlgnp9NugNHXngHl5DSLaSkJ5mZxAafEXM2Rym7tHlgfM6jHdH79vgbZXbEycH0D49J6WBDlYGnFM0Z6kbSKd0RAEYOlI7ejt7itrWAUmpTJ3yaROE+r5gOol2+8G7jxdVuuHyLMhQEWA1iYP7FPd/lJVXEJ6OlO42hHPhTfdPYOuzhc96FOnEcuDJtjVsGpSM0Ugpq0qbeBK7idpSNY1y8RebdG0ceErcdFyFT2rDJCID0r7LA1sOn7TVUN5dBoryvvFDAvX2BOQKR2SNCZcko7aRbld8OyALSGk33Wz919a9Cy8SgT/4ccMvfRM2YVO56EvXX+TAbp9gMlOfkcM64nR5eC0mAb96hYedF1GGZjeJh7aHnGCilkHTY7LIxc28b26S3VxxEO4Ze2MYaQE0+wdND7tUppS6i7DBgk1rUoQy1ykNJDoBXucpQylwkk2Ox4Rm5KPxCQmK9t/6Btpe7qAyIuIMm9o7VGxN+WGGbxHHLhpyPiJ0UYDAgMBAAECggJ9NiWHCgC1fKwMMrRKKavhrJT2JiILZbIEN/mfv3wZoTJbPckY15j6uxmzJcheohSU2dQEtRBZvMBuKdCYFsGC6m1AO/QR3IdOygWr3R2rAKJPVD8afSbs/TcJimpzcgOK46fiPQOr4JIBQx+T5/WM+svP+Z/uxYTgopejPE2JDXDeeUAT+0v9++CaGkllTIc5xJGy6AzCoHAo6S2TDG8G8jd8pyoFPUZHazw+lElBbuKLQG+nz2ULxKsq1yYB6zM35SwlgyGGedC9xJPZyau+RLAL03kjCzJrsfxroqBmZ6kNzP0upGlUa89H1CelzxgihHQjUWVE7/vDnBtW76MDaFt+DXHdNI+lW7Xw6LzQlMUwkU/1DUnAlqZsq+7BqYZw0u+m2xt6gzyQjMUtMvFVaU2HI4ytllrArXSO65FvvhCJ9iZsaUxP44sD+k2OQTHbpC92DC7Tc3fJ4m1PRpWn8l3uh2qv/M1Jrf13j4GG64u7ceZjmJZjwOwc3PbzptS3vwjGtzECfZecOnPb/11ct31sC0GwHW/8mCrZPJXBwVncYa0RW4+BYBQh1pp3Se+5ObQih5PYDOCQc8BMVgq0+zWFePiSYNZwgZ1JMoBzHsZD8LP76hD02U/Cq6ungF2QohKYLxWlptpFED6R6g+2CdiV1J0tPwPN2sjAMo7lq+byhkNybpcY/7HHSkjAXke+1L3KMIf8mhoiEBo7oG8VINrw1B3IVOIyDEquJq3DE/YIQO/9EvUJxukEjNQmG9lS6K9Zlp7JCGCVoWDdxQxUUd3QJsstT9vBsV4XCbzbpdGjOt/zBW07UzufXf7Rfenvk30tbw0qtnBASLYQAQKCAT8L6VAschN31WFy5ICi+fGvkr/5BBB9SXFwCczKXcP8lYbnnUectbQ/kwOZC3flkgdABVcJsxHMnuiKaiHIduTv3PjcIbhLOyKE0qdpB0mvcdiYEFkKLg+/lcpQEUbsUd7knniVI0zWqdW0W0w9/9DTt8I5WYyYDcfMpV0gUQlu/r0ppTFJYU9rLSmO7BPoEcpi9qXm7olINQPw6UJZ/21yMHWo0kgl5jFkL3MvhWoEUo9mhpu6GjFBe5UHrqNnrXT/dW9I1Ibo9oPX/8q4cURqR9H9nQlsyL2vv6WNoCaKYDgyM6MnteszLqC11WmC9sP7/xDGW8Apx7z60x4IiJkq4PhYQQY8UndYP1onC+i3dqIXZzKG8ksvHNLVsOgxIJcVjRPk1CPzCWVfAlIi7jP9nMq5pvyDVmhwuQ2Igk+DAoIBPwsyT69ALJSerz7rAlWPnwmCLvPzFWW/x2VlvKSF9hhLWM4iTdTcaT110NQHfeU5IP7T/GPmrWDtalZ92/Qy/I7prwkbvToXGJqpXAU8oQumkYMbpXDV/m51KFrUMii925195jbPsSygJWnx7Qf1VRatf/i6mDjB8HikRUSEAxvD3nYxHbMsJHyTcOiT13sMV1ks2a1KTt03qmXAHy+9JOtcerxzgC1g6MBSLkS0FnIo5ETa/1BFVml8MV6eahpzzuTZceKqjg0E11Te+uKkltTihwG1EOvhBlJUXaNc7DB4cf/EBsWqE55Bjj6ojk3LvVQbfe71MGMCUkPnl3s//1mmsbYlldJj9p90O4HMK4ulUhjhTcAb1Yyp/aNLV0hw+Ak/FFQiJL3F6kY3K6av/xlnQGJqbXGflHDf+nd454ECggE/Cxg3QB7FOWH8EZr6olLypdmnjBcYEMp+qZ/HK1ro+PSBomyhzjqE+I5D1KKU+7Tnod8XcgZj9tawvHQcoW700OJevhN+aDxda6AWpMJQBOWinAjmFpBOxOGNfsSI7gdX+FQO9sLZiDzKu+IqArmrVficJPTe+qYjSt9f/YDdq5AEu6yNWs8TvT1kryX/aLJhc4pPfukB38rZr+RklBVRaQHwJd4vq3Vc3fdAb6vx0rLftNx+YnqRFHA6oNwugZxUFLnafNjQDF/I8EDmHqQJ2ICq/HHv3G6iF/gHCQJ5w3vZ/BCS0KMmvxaYRKXSUFTuuWKbyladh4zu3iYUnFg3anP/fZybZUZeuUjQ/PufuHr8gZXyvzUhiHFtg/LyYwf7EezE8egS0AYZcMRlovmCXaBUR1ZdbyvlvdDv5Q2TZwKCAT8FsQN0kGiY+7kVgJzlJWawjK6Vb0G3QS94yaiE8VvRqatwzVtHEuukRFltDonelZXCM7wehEop9UWSxdOjZK+hbgR92s3r04W8SM7wEovCwl9BiDInE4CMYZ2r3iXJNAmJG1CFGDp19spN+p6E5x7jsjpVE3UBlQRKLybO11RFLuHMrtbsF0pM0R4Z+90TPYjBWd0lC5rfNMa3hlvUBo/X3GC/nu7PIm3uJVhDsE+gpEy0+6XBBh4Q7QKjmF9TJv9KsYYEtLIUyBs1l0ttHkFV4fjC02nQnch63UvkGP4u/Wrlmp3eqL/kOGq4SzJbNW/+ayMu7xAzpCBxSC5mhnek+OlpL93i3tGk6I6/Qiv4hocfAphlSUDaiibWdRvfRR2whZIt2lVW6l5WZwEXcGt6XFQMMxGDKp1oa1KEnL8BAoIBPwE9NXloDGuy/PTtNrp4cyiGpYFJs73QTHzN5GsJRO2DOrtQDF9/IxWwUcwCGX1mmJqma91xlzrikoPR4+IsR54YqynimLApwYzrPBz7NoPvl2FBZEH0s8UvIGI4OKx4kfdIpTNo8dJ4Ek8ckVCEcdVfqCKhpgZIp4fA89SsbDZi5ox+XHgtDMuoJyPDJdecK9cdrfaNRkkkHq8cE4RG8MdC15WMti6YcfTO1CUlULz41fGSo6bSOP/IvFpfYh13Cu243mzS5uxIhX98hPKz6aOkeSNzUrIIit54nW/HFGTE92874oxCGfE2vaLi5Ty70YPkZVZf0KXepPPWNDsKyYq+U6Msak/MhgyXGulg6jptEBv46KSyMz1ZCD0PR5ghZpgch78RTfYjRI9FZqysjBDeNZTt28HM12WSFINRrXA=";
//            "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCINfILcICBVtIaZMqlOZQYNH2ubxQheMnFFikYjNr8d16iTvPvOlH1FohfKDeXUl4LVmYlOOSBE9br1aCPs6+uMsjx4MtA3MNF/kQyTPQJ6SH1hcVVuhIDMiXpOZWn/r/P/3M3xl0bDAVd/EeM2WOBr6M+96AzTvefBEVpfQyQrLKC7AOf8T1LE9mYf6s0V6WyUNU6oI3kKgYMmtIoLzfkeB1Z94IW1MINWXQfHaD7d0DjRfsA0J2s6aU0UerJ3t+mYXzYTDP5/BXxjSNaiVX1Pd36crDKGCH2+7qPU5GLqadGkT8rmlDZ+AEr/YVquXwEM3XsnMZe615hbHi7c45BAgMBAAECggEAcI1zBnTeGwIs9tZWhRUBBMdLGzp6PfZbc9oN91VdFsVkk2X+N63PT2BhK5BF72qGf4PTP+q1FtvX+mQ6GsR15NHeLdB844oIaevjWlUwSog0IXd6So5ImA92ODP3MCxXP35sLKhjAxh5zuHKFd8EVINXt45S+FLGVsXmvXYxTdHROACkUIfNpoWbMLp1XZgzutJQKDx5slG8+0oagiVZ307drp0ob+UMZHsj5VOV1VZnOfn4533XNgIvs3aIRDYk1FpvWI5MlfJ4kDN2oyj9jks+4M+A2J+chmGwZNZdG8al7Xxg50ElfQp6TOdqpDdlC3hE7meMTZk+/CzWvrxx4QKBgQDGjfvNpM4oq1AFPN3oi9o9tbQYrmlx5eFi4yWGkdGsGvZb9DLyXuIAYHChv7Z7hilKA8d+9fSRnWB3Hnn6+/bk0ReE6yZ55phvLtvC6cyB/IdhHGQaxsainJFBaPVYUROVAkZlsyr7NIUB8A/Ot+MHklfo0Ic4ll4zoYj/C83cjQKBgQCvnnCJGX60FRvG3WC9wEK0zzFKbdgoboL0xLvr5aH8ukqCDgHLGO9mGedCjsqaNSxl7IuL6N32ivyB8tUSw3xAB0dI6Llt7YmvPPCOq4cOiUTF0XY1idgzNN76RIsV4x+lPJN3wn3txy7e8Lz1eAfKwqsrqKbWjKZVCGyqXpQdhQKBgD/G/ZnplRr1JCf6jCEVQw3NXsMKyu399C6qXbRjBGFu67FPuEmn9po+YA9koD3/MyYGHr+GS3+2eHTqwi2/X2fSTeaxDdPcIRydhZOPO9SJBAdKclEzhjxXZEUJbL1olYyohWpHacf078BWlw1EfdVByaEL0lN34VxeR7380axFAoGAc9ZyLDxg0q/JrpZ/JSoX1eOEg6+IkMK0JndN7P9+pZDLbJWIoUpFPnd/jutYv/aq3l/0e8iR//kVLTHbUZEX97PVVUYMaXHxC2GK2tBROUlZz5GmAxJ1RPFu0m07wKgbS4Z1Tt0+6wQi6nKocNFHcnqNJTMNOR9050mzQ302/XECgYEAq/woIKA6GQpFfCJFXfYKmoXsuvguqCwqXhotWRtBUrUR/scJBa/Uh+56Woj20tmY8ozdVaOppCrbh0ukUcPpMJ1gyUdZRgGHXEXjkCT7ftmxOfjg3FGIPFD05CUqM/FOPkD1iXLcfTR7AW5Xy/jVr87RWMynGNDpFo+4cVAYjgM=";
//            "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJhBgzcXBm5A0srvFFu4FsBy+LLW+X0sH/9RvP40VIGOCusY0/CqA65YXWqyQE5jQCegBmnAeVYSvK+3PU4Y1fmr1uiquE6sZB5sl96T0ka+PKzPf4oKoAi6nwLUSenj5xTFjLsFGiuMXrCpMCPImf9JBVk89TJV43Xs3DSNKoj1AgMBAAECgYBsDysCgVv2ChnRH4eSZP/4zGCIBR0C4rs+6RM6U4eaf2ZuXqulBfUg2uRKIoKTX8ubk+6ZRZqYJSo3h9SBxgyuUrTehhOqmkMDo/oa9v7aUqAKw/uoaZKHlj+3p4L3EK0ZBpz8jjs/PXJc77Lk9ZKOUY+T0AW2Fz4syMaQOiETzQJBANF5q1lntAXN2TUWkzgir+H66HyyOpMu4meaSiktU8HWmKHa0tSB/v7LTfctnMjAbrcXywmb4ddixOgJLlAjEncCQQC6Enf3gfhEEgZTEz7WG9ev/M6hym4C+FhYKbDwk+PVLMVR7sBAtfPkiHVTVAqC082E1buZMzSKWHKAQzFL7o7zAkBye0VLOmLnnSWtXuYcktB+92qh46IhmEkCCA+py2zwDgEiy/3XSCh9Rc0ZXqNGD+0yQV2kpb3awc8NZR8bit9nAkBo4TgVnoCdfbtq4BIvBQqR++FMeJmBuxGwv+8n63QkGFQwVm6vCuAqFHBtQ5WZIGFbWk2fkKkwwaHogfcrYY/ZAkEAm5ibtJx/jZdPEF9VknswFTDJl9xjIfbwtUb6GDMc0KH7v+QTBW4GsHwt/gL+kGvLOLcEdLL5rau3IC7EQT0ZYg==";
    private static final String PUBLIC_KEY_STRING = "MIICnzANBgkqhkiG9w0BAQEFAAOCAowAMIIChwKCAn4AhV24uM0KmLjPziDpqMZh1R1ttgjKCsA1+YOJtXQoTlDv/eL4J+M8Yhn/r+ik0J4pZOpH0qMCb2U+Isc3Bt2cXNOdQor8T6ysk/pcY9gN1pCMTRuyYP62TZ+zC24LqlLKCWq8d9I2Oise1kprhY5JbpzQLWk4jEXyhnfSAp/XAQTjAMF5acFvrmYebkD1SAKGCkUwvQiRwMINe4Vts1vHq6BkqbRhqtQsMelPvs4tzFCbgbim3EGIBXRfdtlLcQpX+s4Snp/hea5U3xKcYLoIx1WYR3msm2BukgMOTp/gDu1Fo29BcHOxYwSnNd+dY+LrzRw+3DcEXF609CL5LalcoGS80JYJ6fTboDR154B5eQ0i2kpCeZmcQGnxFzNkcpu7R5YHzOox3R+/b4G2V2xMnB9A+PSelgQ5WBpxTNGepG0indEQBGDpSO3o7e4ra1gFJqUyd8mkThPq+YDqJdvvBu48XVbrh8izIUBFgNYmD+xT3f5SVVxCejpTuNoRz4U33T2Drs4XPehTpxHLgybY1bBqUjNFIKatKm3gSu4naUjWNcvEXm3RtHHhK3HRchU9qwyQiA9K+ywNbDp+01VDeXQaK8r7xQwL19gTkCkdkjQmXJKO2kW5XfDsgC0hpN91s/dfWvQsvEoE/+HHDL30TNmFTuehL11/kwG6fYDJTn5HDOuJ0eXgtJgG/eoWHnRdRhmY3iYe2h5xgopZB02OyyMXNvG9ukt1ccRDuGXtjGGkBNPsHTQ+7VKaUuouwwYJNa1KEMtcpDSQ6AV7nKUMpcJJNjseEZuSj8QkJivbf+gbaXu6gMiLiDJvaO1RsTflhhm8Rxy4acj4idFGAwIDAQAB";
//        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiDXyC3CAgVbSGmTKpTmUGDR9rm8UIXjJxRYpGIza/Hdeok7z7zpR9RaIXyg3l1JeC1ZmJTjkgRPW69Wgj7OvrjLI8eDLQNzDRf5EMkz0Cekh9YXFVboSAzIl6TmVp/6/z/9zN8ZdGwwFXfxHjNljga+jPvegM073nwRFaX0MkKyyguwDn/E9SxPZmH+rNFelslDVOqCN5CoGDJrSKC835HgdWfeCFtTCDVl0Hx2g+3dA40X7ANCdrOmlNFHqyd7fpmF82Ewz+fwV8Y0jWolV9T3d+nKwyhgh9vu6j1ORi6mnRpE/K5pQ2fgBK/2Farl8BDN17JzGXuteYWx4u3OOQQIDAQAB";
//            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYQYM3FwZuQNLK7xRbuBbAcviy1vl9LB//Ubz+NFSBjgrrGNPwqgOuWF1qskBOY0AnoAZpwHlWEryvtz1OGNX5q9boqrhOrGQebJfek9JGvjysz3+KCqAIup8C1Enp4+cUxYy7BRorjF6wqTAjyJn/SQVZPPUyVeN17Nw0jSqI9QIDAQAB";


    public void init() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024);
            KeyPair pair = generator.generateKeyPair();
            privateKey = pair.getPrivate();
            publicKey = pair.getPublic();
        } catch (Exception ignored) {
        }
    }

    public void initFromStrings() {
        try {
            X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(decode(PUBLIC_KEY_STRING));
            PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(decode(PRIVATE_KEY_STRING));

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            publicKey = keyFactory.generatePublic(keySpecPublic);
            privateKey = keyFactory.generatePrivate(keySpecPrivate);
        } catch (Exception ignored) {
        }
    }


    public void printKeys() {
        System.err.println("Public key\n" + encode(publicKey.getEncoded()));
        System.err.println("Private key\n" + encode(privateKey.getEncoded()));
    }

    public String encrypt(String message) {
        try {
            initFromStrings();
            if (publicKey == null) {
                throw new Exception("Public key is null.");
            }

            logger.info("Message to encrypt: " + message);

            byte[] messageToBytes = message.getBytes();
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = cipher.doFinal(messageToBytes);

            return encode(encryptedBytes);
        } catch (Exception e) {
            logger.error("Error during encryption: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
            return null; // Or handle the error appropriately
        }
    }

    private static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private static byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    public String decrypt(String encryptedMessage) {
        try {
            initFromStrings();
            if (privateKey == null) {
                throw new Exception("Private key is null.");
            }

            byte[] encryptedBytes = decode(encryptedMessage);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedMessage = cipher.doFinal(encryptedBytes);

            return new String(decryptedMessage, "UTF8");
        } catch (Exception e) {
            logger.error("Error during decryption: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
            return null; // Or handle the error appropriately
        }
    }
}
