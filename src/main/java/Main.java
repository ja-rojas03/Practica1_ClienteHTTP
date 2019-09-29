import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Document doc;
        Connection conn;
        String url;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite el url: ");
        url = scanner.nextLine();

//        url = "http://itachi.avathartech.com:4567/opcion2.html";
        try {
            conn = Jsoup.connect(url);
            doc = conn.get();
            // A //
            System.out.println("A");
            String bodySize = conn.execute().body();
            int size = bodySize.split("\n").length;
            System.out.println("Number of lines in doc: " + size);
            // B //
            System.out.println("B");

            Elements paragraphs = doc.getElementsByTag("p");
            System.out.println("Number of <p> tags: " +  paragraphs.size());


            // C //
            System.out.println("C");

            int images = 0;
            for(Element element: paragraphs) {
                images += element.getElementsByTag("img").size();
            }
            System.out.println("Number of <img> tags: " +  images);

            // D //
            System.out.println("D");

            Elements forms = doc.getElementsByTag("form");
            int gets = 0, posts = 0;
            for (Element element : forms) {
                if(element.attr("method").equals("get")){
                    gets++;
                }else {
                    posts++;
                }
            }
            System.out.println("GETS: " + gets + " POSTS: " + posts);

            // E //
            System.out.println("E");

            ArrayList<Elements> elements= new ArrayList<>();
            for(Element element : forms) {
                elements.add(element.getElementsByTag("input"));
            }

            for(Elements elements1 : elements) {
                for (Element element: elements1) {
                    System.out.println("Input type: " + element.getElementsByTag("input").attr("type"));
                }
                System.out.println("--");
            }



            // F //
            System.out.println("F");
            makeRequest(doc,url);

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    private static void makeRequest(Document document, String url){
        Elements forms = document.select("form");

        for(Element form : forms) {
            if(form.attr("method").equalsIgnoreCase("post")){
                String urlToPost = formatURLToSend(form, url);
                System.out.println("url: " + urlToPost);
                try{
                    Document response = Jsoup.connect(urlToPost)
                            .data("asignatura", "practica1")
                            .header("matricula", "20160197")
                            .post();

                    System.out.println(response);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private static String formatURLToSend(Element form, String url) {
        String formattedURL = url.substring(0, url.substring(8).indexOf("/") + 8) + form.attr("action");

        return form.attr("action").contains("http")
                ? form.attr("action")
                : formattedURL;
    }


}
