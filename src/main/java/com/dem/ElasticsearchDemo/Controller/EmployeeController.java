package com.dem.ElasticsearchDemo.Controller;

import com.dem.ElasticsearchDemo.Model.Employee;
import com.dem.ElasticsearchDemo.elasticSearchDao.EmployeeElasticSearchDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeElasticSearchDao elasticSearchDao;
    @GetMapping("/getEsEmployee")
    private CompletionStage<List<Employee>>getEmployees(@RequestParam("id")Integer id){
        return elasticSearchDao.searchEmployees(id);
    }

    @PostMapping("/addEsEmployee")
    private CompletionStage<Void>addEmployee(@RequestBody Employee employee){
        return elasticSearchDao.addEmployee(employee)
                .exceptionally(throwable -> {
                    throwable=throwable.getCause();
                return null;
                });
    }

    private static <T>CompletionStage<T>unwrap(CompletionStage<T>throwa){
        CompletableFuture<T>future=new CompletableFuture<>();
        throwa.whenComplete((v,throwable)->{
            if (throwable==null){
                future.complete(v);
            }else {
                future.completeExceptionally(throwable.getCause());
            }

        });
        return future;
    }

}
