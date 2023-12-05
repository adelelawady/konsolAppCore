package com.konsol.core.domain;

import com.konsol.core.service.api.dto.GridModelCollection;
import com.konsol.core.service.api.dto.SysOptions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Pk.
 */
@Document(collection = "System_configuration")
@Getter
@Setter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SystemConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    //  @Field("grid_collection")
    //  private List<GridModelCollection> gridModelCollections = new ArrayList<>();

    @Field("system_options")
    private SysOptions sysOptions = new SysOptions();

    public SysOptions getSysOptions() {
        return sysOptions;
    }

    public void setSysOptions(SysOptions sysOptions) {
        this.sysOptions = sysOptions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
