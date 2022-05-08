import React from 'react';

export function Order(props) {
    const id = props.id;
    const email = props.email;
    const address = props.address;
    const postcode = props.postcode;
    const orderStatus = props.orderStatus;
    const orderItems = props.orderItems;
    const totalMoney = props.totalMoney;

    const handleDetailsClicked = e => {
        props.onClick(id);
    };


    return <>
        <div className="col-2">
            <img className="img-fluid" src="https://i.imgur.com/HKOFQYa.jpeg" alt=""/>
        </div>
        <div className="col">
            <div className="row text-muted">{id}</div>
        </div>
        <div className="col text-center price">{address}</div>
        <div className="col text-center price">{orderStatus}</div>
        <div className="col text-center price">{totalMoney}원</div>
        <div className="col text-end action">
            <button onClick={handleDetailsClicked} className="btn btn-small btn-outline-dark">확인</button>
        </div>
    </>
}
