package com.hyend.pingu.service;

import com.hyend.pingu.dto.PinDetailResponseDTO;
import com.hyend.pingu.dto.PinSaveRequestDTO;
import com.hyend.pingu.dto.PinUpdateRequestDTO;

public interface PinService {

    Long save(PinSaveRequestDTO requestDTO, String username);

    PinDetailResponseDTO findPinById(Long pinid);

    Long update(Long pinid, PinUpdateRequestDTO requestDTO, String username);

    void delete(Long pinid, String username);

}
