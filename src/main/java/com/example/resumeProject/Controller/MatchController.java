package com.example.resumeProject.Controller;

import com.example.resumeProject.Entity.MatchEntity;
import com.example.resumeProject.Service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/match")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

   @PostMapping("/run")
    public ResponseEntity<?> runMatch(@RequestParam Long resumeId, @RequestParam Long jdId){
        MatchEntity match = matchService.matchResumeAndJD(resumeId,jdId);
        //generate suggestions (you can return them in the response by extending MatchEntity DTO)
       Map<String, Object> resp = new HashMap<>();
       resp.put("match",match);
         //optionally compute suggestions live
       List<String> suggestions = matchService.suggestForMatch(match);
       resp.put("suggestions",suggestions);
       return ResponseEntity.ok(resp);
   }

    @GetMapping("/top/{jdId}")
    public ResponseEntity<List<MatchEntity>> topMatches(@PathVariable Long jdId,
                                                        @RequestParam(defaultValue="10") int limit) {
        List<MatchEntity> top = matchService.getTopMatches(jdId,limit);
        return ResponseEntity.ok(top);
    }

}
