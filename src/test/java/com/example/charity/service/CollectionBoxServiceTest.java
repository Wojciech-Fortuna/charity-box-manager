package com.example.charity.service;

import com.example.charity.model.CollectionBox;
import com.example.charity.model.FundraisingEvent;
import com.example.charity.repository.CollectionBoxRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollectionBoxServiceTest {

    @Mock
    private CollectionBoxRepository collectionBoxRepository;

    @Mock
    private MoneyService moneyService;

    @InjectMocks
    private CollectionBoxService collectionBoxService;

    @Test
    void shouldRegisterNewBox_WhenIdentifierIsUnique() {
        CollectionBox box = new CollectionBox();
        box.setIdentifier("BOX-123");

        when(collectionBoxRepository.existsByIdentifier("BOX-123")).thenReturn(false);
        when(collectionBoxRepository.save(box)).thenReturn(box);

        CollectionBox result = collectionBoxService.registerNewBox(box);

        assertEquals("BOX-123", result.getIdentifier());
        verify(collectionBoxRepository).save(box);
    }

    @Test
    void shouldThrowException_WhenIdentifierAlreadyExists() {
        CollectionBox box = new CollectionBox();
        box.setIdentifier("DUPLICATE");

        when(collectionBoxRepository.existsByIdentifier("DUPLICATE")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> collectionBoxService.registerNewBox(box));
        verify(collectionBoxRepository, never()).save(any());
    }

    @Test
    void shouldReturnAllBoxes() {
        List<CollectionBox> mockBoxes = List.of(new CollectionBox(), new CollectionBox());
        when(collectionBoxRepository.findAll()).thenReturn(mockBoxes);

        List<CollectionBox> result = collectionBoxService.listAllBoxes();

        assertEquals(2, result.size());
        verify(collectionBoxRepository).findAll();
    }

    @Test
    void shouldUnregisterBoxAndDeleteMoney() {
        CollectionBox box = new CollectionBox();
        box.setBoxId(1L);

        when(collectionBoxRepository.findById(1L)).thenReturn(Optional.of(box));

        collectionBoxService.unregisterBox(1L);

        verify(moneyService).deleteByBox(box);
        verify(collectionBoxRepository).deleteById(1L);
    }

    @Test
    void shouldThrowException_WhenBoxNotFound() {
        when(collectionBoxRepository.findById(42L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> collectionBoxService.getBox(42L));
    }

    @Test
    void shouldAssignBoxToEvent_WhenBoxIsEmpty() {
        CollectionBox box = mock(CollectionBox.class);
        FundraisingEvent event = new FundraisingEvent();

        when(box.isEmpty()).thenReturn(true);

        collectionBoxService.assignToEvent(box, event);

        verify(box).setEvent(event);
        verify(collectionBoxRepository).save(box);
    }

    @Test
    void shouldThrow_WhenAssigningNonEmptyBox() {
        CollectionBox box = mock(CollectionBox.class);
        FundraisingEvent event = new FundraisingEvent();

        when(box.isEmpty()).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> collectionBoxService.assignToEvent(box, event));
        verify(collectionBoxRepository, never()).save(any());
    }
}
