import {Order} from "./Order";
import React from 'react';

export function OrderList({orders = [], onClick}) {
    return (
        <React.Fragment>
            <h5 className="flex-grow-0"><b>주문 목록</b></h5>
            <ul className="list-group products">
                {orders.map(v =>
                    <li key={v.id} className="list-group-item d-flex mt-3">
                        <Order {...v} onClick={onClick}/>
                    </li>
                )}
            </ul>
        </React.Fragment>
    );
}
