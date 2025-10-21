package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import com.sky.service.UserService;
import com.sky.vo.UserProfileVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user/addressBook")
@Api(tags = "C端地址簿接口")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private UserService userService;

    /**
     * 查询当前登录用户的所有地址信息
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询当前登录用户的所有地址信息")
    public Result<List<AddressBook>> list() {
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);
        return Result.success(list);
    }

    /**
     * 新增地址
     *
     * @param addressBook
     * @return
     */
    @PostMapping
    @ApiOperation("新增地址")
    public Result save(@RequestBody AddressBook addressBook) {
        addressBookService.save(addressBook);
        return Result.success();
    }

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址")
    public Result<AddressBook> getById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * 根据id修改地址
     *
     * @param addressBook
     * @return
     */
    @PutMapping
    @ApiOperation("根据id修改地址")
    public Result update(@RequestBody AddressBook addressBook) {
        addressBookService.update(addressBook);
        return Result.success();
    }

    /**
     * 设置默认地址
     *
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result setDefault(@RequestBody AddressBook addressBook) {
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    /**
     * 根据id删除地址
     *
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("根据id删除地址")
    public Result deleteById(Long id) {
        addressBookService.deleteById(id);
        return Result.success();
    }
    /**
     * 查询默认地址
     */
    @GetMapping("default")
    @ApiOperation("查询默认地址")
    public Result<AddressBook> getDefault() {
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(1);
        List<AddressBook> list = addressBookService.list(addressBook);

        if (list != null && list.size() > 0) {
            return Result.success(list.get(0));
        }

        return Result.error("没有查询到默认地址");
    }

    /**
     * 预置默认地址（只读返回，不入库）
     */
    @GetMapping("/presetDefault")
    @ApiOperation("获取下单默认地址")
    public Result<AddressBook> presetDefault() {
        Long userId = BaseContext.getCurrentId();
        UserProfileVO profile = userService.getCurrentUserProfile(userId);

        if(profile.getPhone() == null){
            throw new RuntimeException("手机号码不能为空！");
        }

        AddressBook preset = AddressBook.builder()
                .id(Long.parseLong(System.currentTimeMillis() + String.valueOf((int)(Math.random() * 10000))))
                .userId(userId)
                .consignee(profile != null && profile.getName() != null ? profile.getName() : "本人")
                .phone(profile.getPhone())
                .sex(profile != null ? profile.getSex() : String.valueOf(1))
                // 区划编号
                .provinceCode("450000")
                .provinceName("广西壮族自治区")
                .cityCode("450300")
                .cityName("桂林市")
                .districtCode("450323")
                .districtName("灵川县")
                // 详细地址
                .detail("桂林电子科技大学(花江校区)")
                .label("默认")
                .isDefault(1)
                .build();

        try {
            // 写入数据库为当前用户默认地址
            addressBookService.saveOrUpdatePresetDefault(preset);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return Result.success(preset);
    }
}
