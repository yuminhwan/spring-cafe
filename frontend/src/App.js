import './App.css';
import 'bootstrap/dist/css/bootstrap.css'
import React, {useEffect, useState} from 'react';
import {ProductList} from "./components/ProductList";
import {OrderList} from "./components/OrderList";
import {Summary} from "./components/Summary";
import axios from "axios";
import {BrowserRouter, Route, Routes} from "react-router-dom";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Order/>}/>
                <Route path="/history" element={<Form/>}/>
            </Routes>
        </BrowserRouter>
    )
}

function Order() {
    const [products, setProducts] = useState([
        {id: '1', name: '콜롬비아 커피1', category: '커피빈', price: 5000, stock: 100}
    ]);

    const historyClick = () => {
        document.location.href = "/history";
    }

    const [items, setItems] = useState([]);
    const handleAddClicked = productId => {
        const product = products.find(v => v.id === productId);
        const found = items.find(v => v.id === productId);
        const updatedItems =
            found ? items.map(v => (v.id === productId) ? {...v, count: v.count + 1} : v) : [...items, {
                ...product,
                count: 1
            }]
        setItems(updatedItems)
        product.stock = product.stock - 1;
    }

    useEffect(() => {
        axios.get('http://localhost:8080/api/v1/products')
            .then(v => setProducts(v.data));
    }, []);

    const handleOrderSubmit = (order) => {
        if (items.length === 0) {
            alert("아이템을 추가해 주세요!");
        } else {
            axios.post('http://localhost:8080/api/v1/orders', {
                email: order.email,
                address: order.address,
                postcode: order.postcode,
                orderItems: items.map(v => ({
                    productId: v.id,
                    price: v.price,
                    quantity: v.count
                }))
            }).then(() => alert("주문이 정상적으로 접수되었습니다."), e => {
                alert(e.response.data.errorMessage);
                console.error(e);
            })
        }
    }

    return (
        <div className="container-fluid">
            <div className="row justify-content-center m-4">
                <h1 className="text-center">React Cafe</h1>
            </div>
            <div className="card">
                <div className="row">
                    <div className="col-md-8 mt-4 d-flex flex-column align-items-start p-3 pt-0">
                        <ProductList products={products} onAddClick={handleAddClicked}/>
                    </div>
                    <div className="col-md-4 summary p-4">
                        <Summary items={items} onOrderSubmit={handleOrderSubmit}/>
                        <button onClick={historyClick}>주문 내역</button>
                    </div>
                </div>
            </div>
        </div>
    );

}

function Form() {

    let email = "";
    const [orders, setOrders] = useState([
        {
            id: '1',
            email: 'hwan@gmail.com',
            address: '경북 ',
            orderStatus: 'Accpet',
            orderItems: ['asd'],
            totalMoney: 1000,
            postcode: "1234"
        }
    ]);

    const onChangeEmail = (e) => {
        email = e.target.value;
    };

    async function fetchOrders() {
        const base = 'http://localhost:8080/api/v1/orders';
        axios.get(
            'http://localhost:8080/api/v1/orders/',
            {params: {email: email}}
        ).then(v => setOrders(v.data))
    }

    const onClick = orderId => {
    }

    return (

        <div className="container-fluid">
            <div className="row justify-content-center m-4">
                <h1 className="text-center">React Cafe</h1>
            </div>
            <div className="card">
                <form>
                    <div className="mb-3">
                        <input type="email" className="form-control mb-1" id="email" placeholder="이메일을 입력해주세요."
                               onChange={onChangeEmail}/>
                    </div>
                </form>
                <button className="btn btn-dark col-12" onClick={fetchOrders}>확인하기</button>
                <div className="align-items-start p-3 pt-0">
                    <OrderList orders={orders} onClick={onClick}/>
                </div>
            </div>
        </div>
    );
}

export default App;
