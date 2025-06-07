package com.bytescheduler.adminx.modules.system.dto;

import com.bytescheduler.adminx.modules.system.entity.Menu;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class MenuTreeRequest extends Menu {
    private List<MenuTreeRequest> children;
}