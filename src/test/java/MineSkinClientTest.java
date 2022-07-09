import com.molean.velocityskinloader.client.MineSkinClient;
import com.molean.velocityskinloader.model.mineskin.Delay;
import com.molean.velocityskinloader.model.mineskin.GenerateByUrl;
import com.molean.velocityskinloader.model.mineskin.SkinInfo;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

public class MineSkinClientTest {

    @Test
    public void generateByUploadTest() {
        MineSkinClient mineSkinClient = MineSkinClient.instance();
        try (InputStream resourceAsStream = MineSkinClientTest.class.getClassLoader().getResourceAsStream("test.png")) {
            assert resourceAsStream != null;
            byte[] bytes = resourceAsStream.readAllBytes();
            SkinInfo skinInfo = mineSkinClient.generateByUpload(null, null, null, bytes);
            assert skinInfo != null;
            System.out.println(skinInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void generateByUrl() throws Exception {
        MineSkinClient mineSkinClient =  MineSkinClient.instance();
        GenerateByUrl generateByUrl = new GenerateByUrl();
        generateByUrl.setUrl("https://skin.prinzeugen.net/textures/299e79c5b2ae610937fb7af4219938f37bfd79e1c89f2cd2bcb2ce6ab9f721fb");
        SkinInfo skinInfo = mineSkinClient.generateByUrl(generateByUrl);
        assert skinInfo != null;
        System.out.println(skinInfo);
    }

    @Test
    public void getDelay() throws Exception {
        MineSkinClient mineSkinClient = MineSkinClient.instance();
        Delay delay = mineSkinClient.getDelay();
        assert delay != null;
        System.out.println(delay);
    }
}
