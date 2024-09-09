package com.searcher.searcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

//не предназначен для распределения по потокам
public class PicsSaver {

    private static final Logger LOGGER = LogManager.getLogger(PicsSaver.class);
    private final static String BASE_URL = "https://yandex.ru";
    private final static String ARCHIVE_URL = "/archive/catalog";
    private final String localPathToSave;
    private final String docId;
    private final int page;

    public PicsSaver(String localPathToSave, String docId, int page) {
        this.localPathToSave = localPathToSave;
        this.docId = docId;
        this.page = page;
    }

    public void save() throws IOException {

        createDirIfNotExists();

        Optional<Document> document = getHtml(page);
        if (document.isPresent()) {
            String imagePath = BASE_URL + getImagePath(document);

            try (InputStream in = new URL((imagePath)).openStream()) {
                String fullPath = localPathToSave + docId + "\\" + page + ".jpg";
                Files.copy(in, Paths.get(fullPath));
            }
        } else {
            LOGGER.info("Не существует: " + page);
        }
    }

    private void createDirIfNotExists() {
        File theDir = new File(localPathToSave + docId);
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
    }

    private String getImagePath(Optional<Document> document) {
        String nextData__ = document.get().getElementById("__NEXT_DATA__").childNode(0).attributes().get("#data");
        JSONObject jsonObj = new JSONObject(nextData__);
        JSONObject props = (JSONObject) jsonObj.get("props");
        JSONObject pageProps = (JSONObject) props.get("pageProps");
        JSONObject currentNode = (JSONObject) pageProps.get("currentNode");
        return (String) currentNode.get("originalImagePath");
    }


    private Optional<Document> getHtml(int pageNumber) throws IOException {
        String url = createUrl(pageNumber);
        LOGGER.info("Стучусь к " + url);
        Document document = Jsoup.connect(url).maxBodySize(0).get();
        return Optional.of(document);
    }

    private String createUrl(int page) {
        return BASE_URL + ARCHIVE_URL + "/" + docId + "/" + page;
    }

    @Override
    public String toString() {
        return "PicsSaver{}";
    }
}
