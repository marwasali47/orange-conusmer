
package com.orange.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Marwa Ali on 2020-06-10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attachment {

    private String Id;
    private String name;
    private String type;
    private String size;
}
