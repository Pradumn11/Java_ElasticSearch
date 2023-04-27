package com.dem.ElasticsearchDemo.Controller;

import com.dem.ElasticsearchDemo.Model.School;
import com.dem.ElasticsearchDemo.elasticSearchDao.SchoolEsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/school")
public class SchoolController {

    @Autowired
    SchoolEsDao schoolEsDao;

    @GetMapping("/getByType")
    public CompletionStage<List<School>>getByType(@RequestParam("type")String type){
      return schoolEsDao.searchSchoolByType(type);
    }

    @GetMapping("/getByAttributes")
    public CompletionStage<List<School>>getByAttributes(@RequestParam("adharCard")Long adhar,
                                                        @RequestParam("panCard")Long pan){

        return schoolEsDao.getByAdharOrPanCard(adhar,pan);
    }

}
