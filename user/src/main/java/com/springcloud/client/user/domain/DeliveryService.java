package com.springcloud.client.user.domain;

import com.springcloud.client.user.interfaces.UserInfoHeader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryReader deliveryReader;
    private final DeliveryStore deliveryStore;
    private final UserReader userReader;
    private static final DeliveryDriverRole CURRENT_DRIVER_INDEX_FOR_HUB = DeliveryDriverRole.HUB;
    private static final DeliveryDriverRole CURRENT_DRIVER_INDEX_FOR_COMPANY = DeliveryDriverRole.COMPANY;
    private static final List<Integer> ALLOWED_PAGE_SIZES = List.of(10, 30, 50);
    private static final int DEFAULT_PAGE_SIZE = 10;

    @Transactional
    public DeliveryInfo registerHubDeliveryDriver(UserInfoHeader userInfoHeader, DeliveryCommand command) {
        if (userInfoHeader.getUserRole() == UserRole.HUB_MANAGER || userInfoHeader.getUserRole() == UserRole.DELIVERY_STAFF || userInfoHeader.getUserRole() == UserRole.COMPANY_MANAGER) {
            throw new IllegalArgumentException("허브 배송 담당자를 등록할 수 있는 권한이 없습니다.");
        }

        User user = userReader.findById(command.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Integer maxOrderNumber = deliveryReader.findMaxDeliveryOrderNumberByRole(DeliveryDriverRole.HUB);
        int newOrderNumber = (maxOrderNumber == null) ? 0 : maxOrderNumber + 1;

        user.updateRole(UserRole.DELIVERY_STAFF);
        DeliveryDriver deliveryDriver = command.toEntity(user, newOrderNumber);

        DeliveryDriver savedDriver = deliveryStore.save(deliveryDriver);
        return new DeliveryInfo(savedDriver);
    }

    @Transactional
    public DeliveryInfo getHubDeliveryDriver() {
        List<DeliveryDriver> drivers = deliveryReader.findAllByRoleOrderByDeliveryOrderNumberAsc(DeliveryDriverRole.HUB);

        if (drivers.isEmpty()) {
            throw new IllegalStateException("등록된 허브 배송 담당자가 없습니다.");
        }

        DeliveryAssignment assignment = deliveryReader.findWithLock(CURRENT_DRIVER_INDEX_FOR_HUB);

        DeliveryDriver deliveryDriver = drivers.get(assignment.getCurrentDriverIndex());

        int nextDriverIndex = (assignment.getCurrentDriverIndex() + 1) % drivers.size();
        assignment.updateCurrentDriverIndex(nextDriverIndex);
        deliveryStore.save(assignment);

        return new DeliveryInfo(deliveryDriver);
    }

    public DeliveryInfo registerCompanyDeliveryDriver(UserInfoHeader userInfoHeader, DeliveryCommand command) {
        if (userInfoHeader.getUserRole() == UserRole.DELIVERY_STAFF || userInfoHeader.getUserRole() == UserRole.COMPANY_MANAGER) {
            throw new IllegalArgumentException("업체 배송 담당자를 등록할 수 있는 권한이 없습니다.");
        }

        if (userInfoHeader.getUserRole() == UserRole.HUB_MANAGER && userInfoHeader.getHubId() != command.getHubId()) {
            throw new IllegalArgumentException("해당 허브의 업체 배송 담당자를 등록할 수 있는 권한이 없습니다. 허브 관리자가 소속된 허브의 업체 배송 담당자만 등록할 수 있습니다.");
        }

        User user = userReader.findById(command.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Integer maxOrderNumber = deliveryReader.findMaxDeliveryOrderNumberByRole(DeliveryDriverRole.COMPANY);
        int newOrderNumber = (maxOrderNumber == null) ? 0 : maxOrderNumber + 1;

        user.updateRole(UserRole.DELIVERY_STAFF);
        DeliveryDriver deliveryDriver = command.toEntity(user, newOrderNumber);

        DeliveryDriver savedDriver = deliveryStore.save(deliveryDriver);
        return new DeliveryInfo(savedDriver);
    }

    @Transactional
    public DeliveryInfo getCompanyDeliveryDriver(UUID hubId) {
        List<DeliveryDriver> drivers = deliveryReader.findAllByHubIdAndRoleOrderByDeliveryOrderNumberAsc(hubId, DeliveryDriverRole.COMPANY);
        if (drivers.isEmpty()) {
            throw new IllegalStateException("해당 허브에 등록된 업체 배송 담당자가 없습니다.");
        }

        DeliveryAssignment assignment = deliveryReader.findWithLock(CURRENT_DRIVER_INDEX_FOR_COMPANY);

        DeliveryDriver deliveryDriver = drivers.get(assignment.getCurrentDriverIndex());

        int nextDriverIndex = (assignment.getCurrentDriverIndex() + 1) % drivers.size();
        assignment.updateCurrentDriverIndex(nextDriverIndex);
        deliveryStore.save(assignment);

        return new DeliveryInfo(deliveryDriver);
    }

    public void deleteDeliveryDriver(UserInfoHeader userInfoHeader, DeliveryCommand command) {
        if (userInfoHeader.getUserRole() == UserRole.DELIVERY_STAFF || userInfoHeader.getUserRole() == UserRole.COMPANY_MANAGER) {
            throw new IllegalArgumentException("배송 담당자를 삭제할 수 있는 권한이 없습니다.");
        }

        if (userInfoHeader.getUserRole() == UserRole.HUB_MANAGER && userInfoHeader.getHubId() != command.getHubId()) {
            throw new IllegalArgumentException("해당 허브의 배송 담당자를 삭제할 수 있는 권한이 없습니다. 허브 관리자가 소속된 허브의 배송 담당자만 삭제할 수 있습니다.");
        }

        DeliveryDriver deliveryDriver = deliveryReader.findById(command.getUserId())
                .filter(d -> d.getDeletedAt() == null) // 삭제되지 않은 배송 담당자 필터링
                .orElseThrow(() -> new IllegalArgumentException("배송 담당자를 찾을 수 없거나 삭제된 배송 담당자입니다."));

        deliveryDriver.delete();
        deliveryStore.save(deliveryDriver);
    }

    @Transactional
    public DeliveryInfo updateDeliveryDriver(UserInfoHeader userInfoHeader, DeliveryCommand command) {
        if (userInfoHeader.getUserRole() == UserRole.DELIVERY_STAFF || userInfoHeader.getUserRole() == UserRole.COMPANY_MANAGER) {
            throw new IllegalArgumentException("배송 담당자 정보를 수정할 수 있는 권한이 없습니다.");
        }

        if (userInfoHeader.getUserRole() == UserRole.HUB_MANAGER && userInfoHeader.getHubId() != command.getHubId()) {
            throw new IllegalArgumentException("해당 허브의 배송 담당자 정보를 수정할 수 있는 권한이 없습니다. 허브 관리자가 소속된 허브의 배송 담당자 정보만 수정할 수 있습니다.");
        }

        DeliveryDriver deliveryDriver = deliveryReader.findById(command.getDeliveryDriverId())
                .filter(d -> d.getDeletedAt() == null) // 삭제되지 않은 배송 담당자 필터링
                .orElseThrow(() -> new IllegalArgumentException("배송 담당자를 찾을 수 없거나 삭제된 배송 담당자입니다."));

        deliveryDriver.update(command);

        return new DeliveryInfo(deliveryDriver);
    }

    public DeliveryInfo getDeliveryDriver(UserInfoHeader userInfoHeader, UUID deliveryDriverId) {
        if (userInfoHeader.getUserRole() == UserRole.COMPANY_MANAGER) {
            throw new IllegalArgumentException("배송 담당자 정보를 조회할 수 있는 권한이 없습니다.");
        }

        DeliveryDriver deliveryDriver = deliveryReader.findById(deliveryDriverId)
                .filter(d -> d.getDeletedAt() == null) // 삭제되지 않은 배송 담당자 필터링
                .orElseThrow(() -> new IllegalArgumentException("배송 담당자를 찾을 수 없거나 삭제된 배송 담당자입니다."));

        return new DeliveryInfo(deliveryDriver);
    }

    public Page<DeliveryInfo> getAllDeliveryDrivers(UserInfoHeader userInfoHeader, int page, int size, String sortBy, String order) {
        if (userInfoHeader.getUserRole() == UserRole.COMPANY_MANAGER) {
            throw new IllegalArgumentException("전체 배송 담당자를 조회할 수 있는 권한이 없습니다.");
        }

        if (!ALLOWED_PAGE_SIZES.contains(size)) {   // 허용된 페이지 사이즈가 아닌 경우, 기본 페이지 사이즈로 설정
            size = DEFAULT_PAGE_SIZE;
        }

        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return deliveryReader.findAll(pageable).map(DeliveryInfo::new);
    }

    public Page<DeliveryInfo> searchDeliveryDrivers(UserInfoHeader userInfoHeader, String keyword, int page, int size, String sortBy, String order) {
        if (userInfoHeader.getUserRole() != UserRole.COMPANY_MANAGER) {
            throw new IllegalArgumentException("배송 담당자 검색 권한이 없습니다.");
        }

        if (!ALLOWED_PAGE_SIZES.contains(size)) {   // 허용된 페이지 사이즈가 아닌 경우, 기본 페이지 사이즈로 설정
            size = DEFAULT_PAGE_SIZE;
        }

        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return deliveryReader.findByUser_UsernameContainingAndDeletedAtIsNull(keyword, pageable).map(DeliveryInfo::new);
    }
}