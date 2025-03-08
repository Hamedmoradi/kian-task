package com.example.kiantask;

import com.example.kiantask.enums.TransactionTypeEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class TransactionTypeEnumTest {

    @Test
    void testEnumValues() {

        TransactionTypeEnum[] values = TransactionTypeEnum.values();

        assertEquals(4, values.length, "Enum should have exactly 4 values");
        assertArrayEquals(new TransactionTypeEnum[]{
                TransactionTypeEnum.TRANSFER,
                TransactionTypeEnum.TRANSFER_IN,
                TransactionTypeEnum.DEPOSIT,
                TransactionTypeEnum.WITHDRAW
        }, values, "Enum values should match expected order");
    }

    @Test
    void testGetValue() {

        assertEquals("TRANSFER", TransactionTypeEnum.TRANSFER.getValue(), "TRANSFER value should match");
        assertEquals("TRANSFER_IN", TransactionTypeEnum.TRANSFER_IN.getValue(), "TRANSFER_IN value should match");
        assertEquals("DEPOSIT", TransactionTypeEnum.DEPOSIT.getValue(), "DEPOSIT value should match");
        assertEquals("WITHDRAW", TransactionTypeEnum.WITHDRAW.getValue(), "WITHDRAW value should match");
    }

    @Test
    void testValueOf() {

        assertEquals(TransactionTypeEnum.TRANSFER, TransactionTypeEnum.valueOf("TRANSFER"), "valueOf should return TRANSFER");
        assertEquals(TransactionTypeEnum.TRANSFER_IN, TransactionTypeEnum.valueOf("TRANSFER_IN"), "valueOf should return TRANSFER_IN");
        assertEquals(TransactionTypeEnum.DEPOSIT, TransactionTypeEnum.valueOf("DEPOSIT"), "valueOf should return DEPOSIT");
        assertEquals(TransactionTypeEnum.WITHDRAW, TransactionTypeEnum.valueOf("WITHDRAW"), "valueOf should return WITHDRAW");
    }

    @Test
    void testValueOfInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> TransactionTypeEnum.valueOf("INVALID"),
                "valueOf with invalid name should throw IllegalArgumentException");
    }

    @Test
    void testEnumToString() {

        assertEquals("TRANSFER", TransactionTypeEnum.TRANSFER.toString(), "toString should return enum name TRANSFER");
        assertEquals("TRANSFER_IN", TransactionTypeEnum.TRANSFER_IN.toString(), "toString should return enum name TRANSFER_IN");
        assertEquals("DEPOSIT", TransactionTypeEnum.DEPOSIT.toString(), "toString should return enum name DEPOSIT");
        assertEquals("WITHDRAW", TransactionTypeEnum.WITHDRAW.toString(), "toString should return enum name WITHDRAW");
    }

    @Test
    void testEnumNameMatchesValue() {

        for (TransactionTypeEnum type : TransactionTypeEnum.values()) {
            assertEquals(type.name(), type.getValue(), "Enum name should match its value for " + type);
        }
    }
}