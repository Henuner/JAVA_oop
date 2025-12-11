package com.gm.wj.controller;

import com.gm.wj.result.Result;
import com.gm.wj.result.ResultFactory;
import com.gm.wj.service.JotterCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class JotterCollectionController {
    @Autowired
    JotterCollectionService collectionService;

    // 1. 核心接口：点击收藏/取消收藏
    @PostMapping("/api/collection")
    public Result toggleCollection(@RequestBody Map<String, Integer> data) {
        int uid = data.get("uid");
        int aid = data.get("aid");

        // 判断当前状态：如果已收藏就删除，没收藏就添加
        if (collectionService.isCollected(uid, aid)) {
            collectionService.removeCollection(uid, aid);
            return ResultFactory.buildSuccessResult("取消收藏成功");
        } else {
            collectionService.addCollection(uid, aid);
            return ResultFactory.buildSuccessResult("收藏成功");
        }
    }

    // 2. 状态接口：进页面时查询是否高亮星星
    @GetMapping("/api/collection/status")
    public Result checkStatus(@RequestParam int uid, @RequestParam int aid) {
        boolean isCollected = collectionService.isCollected(uid, aid);
        return ResultFactory.buildSuccessResult(isCollected);
    }

    // 3. 【新增接口】根据用户名查询收藏列表
    // 访问路径: /api/collection/list?username=lxm
    @GetMapping("/api/collection/list")
    public Result listCollections(@RequestParam String username) {
        return ResultFactory.buildSuccessResult(collectionService.findCollectionsByUsername(username));
    }
}
