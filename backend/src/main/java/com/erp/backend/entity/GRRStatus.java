package com.erp.backend.entity;

public enum GRRStatus {

    CREATED,     // GRR created but goods not yet received

    RECEIVED,    // Goods received and stock will increase (Day 16)

    CANCELLED    // GRR cancelled (no stock change)
}
