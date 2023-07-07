package com.kenden.kendenrmapp.models;

public class Reader {
    public String id, dName, dDob, dEduc, dWork, dAddress, dPas;

    public Reader() {
    }

    public Reader(String id, String dName, String dDob, String dEduc, String dWork, String dAddress, String dPas) {
        this.id = id;
        this.dName = dName;
        this.dDob = dDob;
        this.dEduc = dEduc;
        this.dWork = dWork;
        this.dAddress = dAddress;
        this.dPas = dPas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getdDob() {
        return dDob;
    }

    public void setdDob(String dDob) {
        this.dDob = dDob;
    }

    public String getdEduc() {
        return dEduc;
    }

    public void setdEduc(String dEduc) {
        this.dEduc = dEduc;
    }

    public String getdWork() {
        return dWork;
    }

    public void setdWork(String dWork) {
        this.dWork = dWork;
    }

    public String getdAddress() {
        return dAddress;
    }

    public void setdAddress(String dAddress) {
        this.dAddress = dAddress;
    }

    public String getdPas() {
        return dPas;
    }

    public void setdPas(String dPas) {
        this.dPas = dPas;
    }
}
