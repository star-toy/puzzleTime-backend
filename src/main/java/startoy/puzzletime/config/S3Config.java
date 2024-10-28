package startoy.puzzletime.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

/*    @Bean
    public AmazonS3 amazonS3() {

        String accessKey = System.getenv("AWS_ACCESS_KEY");
        String secretKey = System.getenv("AWS_SECRET_KEY");
        String region = System.getenv("AWS_REGION");


            AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

            return AmazonS3ClientBuilder
                    .standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(region)
                    .build();
        }*/
    }
