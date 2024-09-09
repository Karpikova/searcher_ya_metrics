package com.searcher.searcher;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class SearcherController {

    //http://localhost:8080/search?docId=c66ee56a-8eb9-4629-8e02-e9720d9608b6&pageFrom=500&pageTo=502
        //http://localhost:8080/search?docId=43d70276-5e8c-4389-9070-70ad4f6f7c61&pageFrom=1145&pageTo=1146
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity doSearch(@RequestParam(value = "docId") String docId,
                                   @RequestParam(value = "pageFrom") int pageFrom,
                                   @RequestParam(value = "pageTo") int pageTo) throws IOException {
        String localPathToSave = "C:\\metrica\\";
        for (int i = pageFrom; i <= pageTo; i++) {
            PicsSaver picsSaver = new PicsSaver(localPathToSave, docId, i);
            picsSaver.save();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
