package com.springcloud.management.domain.repository;

import com.springcloud.management.application.dto.DeleteSlackCommand;
import com.springcloud.management.application.dto.UpdateSlackCommand;
import com.springcloud.management.domain.entity.Slack;

public interface SlackStore {
    Slack save(Slack slack);
    Slack save(UpdateSlackCommand updateSlackCommand);
    Slack save(DeleteSlackCommand deleteSlackCommand);
}
