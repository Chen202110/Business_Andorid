package com.chen.bean;

import com.chen.vo.CartVo;

public class MessageEvent {

    private CartVo cartVo;

    public MessageEvent(CartVo cartVo){
        this.cartVo = cartVo;
    }

    public CartVo getCartVo() {
        return cartVo;
    }

    public void setCartVo(CartVo cartVo) {
        this.cartVo = cartVo;
    }
}
