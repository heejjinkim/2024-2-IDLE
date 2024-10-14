package com.example.__2_IDLE.ros;

import com.example.__2_IDLE.ros.data_listener.ROSDataListener;
import com.example.__2_IDLE.ros.message_handler.ROSMessageHandler;
import com.example.__2_IDLE.ros.message_value.MessageValue;
import lombok.AllArgsConstructor;

import java.util.concurrent.CountDownLatch;

@AllArgsConstructor
public class ROSValueGetter<T extends MessageValue> {

    private ROSDataListener dataListener;
    private ROSMessageHandler<T> messageHandler;

    public T getValue() {
        // 동기 처리를 위한 CountDownLatch 설정
        CountDownLatch latch = new CountDownLatch(1);

        // 데이터 수신 시작
        dataListener.connect();
        dataListener.go();

        // 비동기 처리 후 동기화
        new Thread(() -> {
            // processMessage 실행
            messageHandler.processMessage();
            latch.countDown();  // processMessage 완료 시 latch 해제
        }).start();

        try {
            latch.await();  // processMessage가 끝날 때까지 대기
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Process was interrupted", e);
        }
        T value = messageHandler.getValue();
        return value;
    }
}
