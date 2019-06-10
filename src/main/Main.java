package main;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main extends Application {

    private ConfigurableApplicationContext context;
    private Parent rootNode;

    @Value("${ssh.tunnel.url}")
    private String url;

    @Value("${ssh.tunnel.username}")
    private String username;

    @Value("${ssh.tunnel.password}")
    private String password;

    @Value("${ssh.tunnel.port:22}")
    private int port;

    private Session session;

    @Override
    public void init() throws Exception {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Main.class);
        context = builder.run(getParameters().getRaw().toArray(new String[0]));

//        //create SSH tonnel
//        JSch jsch = new JSch();
//        // Get SSH session
//        session = jsch.getSession(username, url, port);
//        session.setPassword(password);
//        java.util.Properties config = new java.util.Properties();
//        // Never automatically add new host keys to the host file
//        config.put("StrictHostKeyChecking", "no");
//        session.setConfig(config);
//        // Connect to remote server
//        session.connect();
//        // Apply the port forwarding
//        session.setPortForwardingL(lport, rhost, rport);


        FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/main.fxml"));
        loader.setControllerFactory(context::getBean);
        rootNode = loader.load();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //get current screen resolution
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double width = visualBounds.getWidth();
        double height = visualBounds.getHeight();

        primaryStage.setScene(new Scene(rootNode, 800, 600));
        primaryStage.setTitle("CrawlCrewⓇ  Team");
        primaryStage.centerOnScreen();
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        context.close();
    }
}