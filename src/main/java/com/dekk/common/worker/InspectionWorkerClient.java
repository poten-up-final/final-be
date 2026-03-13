package com.dekk.common.worker;

public interface InspectionWorkerClient {
    void sendInspectionRequest(Long productImageId, String originUrl, String imageUrl);
}
