package com.nicky.handleradapter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * @author nicky_chin [shuilianpiying@163.com]
 * @since --created on 2018/8/20 at 16:25
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModelAndView {

    private HttpStatus status;

    private Map<String, Object> model;

}
