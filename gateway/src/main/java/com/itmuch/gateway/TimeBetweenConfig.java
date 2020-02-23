package com.itmuch.gateway;

import lombok.Data;

import java.time.LocalTime;

/**
 * @author admin
 */
@Data
public class TimeBetweenConfig {
    private LocalTime start;
    private LocalTime end;
}
