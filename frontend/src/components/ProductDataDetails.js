import React, {Component} from 'react';
import moment from 'moment';
import axios from 'axios';

import ProductData from '../containers/ProductData';
import './ProductDataDetails.css';

class ProductDataDetails extends Component {

    axiosConfig = {
        headers: {
            'Content-Type': 'application/json;charset=UTF-8',
            "Access-Control-Allow-Origin": "*"
        }
    };

    state = {
        productDataResults: []
    };

    constructor(props) {
        super(props);
        this.state = {}
    }

    componentDidMount() {
        this.getProductData()
    }

    getProductData() {
        axios.get('http://localhost:8080/api/productData/', this.axiosConfig)
            .then(response => {
                this.setState({productDataResults: response.data});
                console.log(response);
            })
    }

    sameDate(date1, date2) {
        return moment(date1).isSame(moment(date2), 'year')
            && moment(date1).isSame(moment(date2), 'month')
            && moment(date1).isSame(moment(date2), 'day')
    }

    renderColumnHeaders() {
        return <ProductData
            key='header'
            product='Product'
            provider='Advertiser'
            date='Date'
            clicks='Clicks'/>
    }

    renderColumnRows() {
        return this.state.productDataResults.map(
            productData => {
                if (this.props.productId == null || productData.product.id === this.props.productId) {
                    if (this.props.providerId == null || productData.provider.id === this.props.providerId) {
                        if (this.props.date == null || this.sameDate(productData.dateRecorded, this.props.date)) {
                            return <ProductData
                                key={productData.id}
                                product={productData.product.name}
                                provider={productData.provider.name}
                                date={moment(productData.dateRecorded).format('YYYY-MM-DD')}
                                clicks={productData.clicks}/>
                        }
                    }
                }
            }
        );
    }

    render() {
        if (!this.state.productDataResults) {
            return (<p>Loading Data</p>)
        }

        return (
            <div>
                {this.renderColumnHeaders()}
                {this.renderColumnRows()}
            </div>
        );
    }
}

export default ProductDataDetails;