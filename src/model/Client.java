/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author HP
 */
public class Client implements Serializable{
    private int id;
    private String name;
    private String password;
    private int deposit_value;

    public Client(String name, String password) {
        this.name = name;
        this.password = password;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDeposit_value() {
        return deposit_value;
    }

    public void setDeposit_value(int deposit_value) {
        this.deposit_value = deposit_value;
    }

  
}
