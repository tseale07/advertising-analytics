import React, {Component} from 'react';
import Panel from 'react-bootstrap/lib/Panel'
import Button from 'react-bootstrap/lib/Button'
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import ProductDataDetails from './ProductDataDetails'
import axios from 'axios'

export default class ProductDataPage extends Component {

    axiosConfig = {
        headers: {
            'Content-Type': 'application/json;charset=UTF-8',
            "Access-Control-Allow-Origin": "*"
        }
    };

    state = {
        productDataResults: null,
        productsResults: null,
        providersResults: null,
        selectedProduct: null,
        selectedProvider: null,
        selectedDate: null
    };

    constructor(props) {
        super(props)
        this.handleChange = this.handleChange.bind(this);
        this.state = {}
    }

    handleChange(date) {
        this.setState({selectedDate: date})
    }

    //function which is called the first time the component loads
    componentDidMount() {
        this.getProducts();
        this.getProviders();
        this.getProductData();
    }

    getProductData() {
        axios.get('http://localhost:8080/api/productData/', this.axiosConfig)
            .then(response => {
                this.setState({productDataResults: response.data});
                console.log(response);
            })
    }

    getProducts() {
        axios.get('http://localhost:8080/api/products/', this.axiosConfig)
            .then(response => {
                this.setState({productsResults: response.data});
                console.log(response);
            })
    }

    getProviders() {
        axios.get('http://localhost:8080/api/providers/', this.axiosConfig)
            .then(response => {
                this.setState({providersResults: response.data});
                console.log(response);
            })
    }

    renderProductSelector() {
        return <div className="col-md-1">
            <Panel bsStyle="default" className="ProductPanel">
                <Panel.Heading>
                    <Panel.Title componentClass="h3">Products</Panel.Title>
                </Panel.Heading>
                <Panel.Body>
                    <Button bsStyle="default" className="ProductButton" onClick={() => this.setState({selectedProduct: null})}>
                        All
                    </Button>
                </Panel.Body>
                {
                    this.state.productsResults.map(
                        product =>
                            <Panel.Body>
                                <Button bsStyle="default" className="ProductButton" onClick={() => this.setState({selectedProduct: product.id})}>
                                    {product.name}
                                </Button>
                            </Panel.Body>
                    )
                }
            </Panel>
        </div>
    }

    renderProviderSelector() {
        return <div className="col-md-2">
            <Panel bsStyle="default" className="ProviderPanel">
                <Panel.Heading>
                    <Panel.Title componentClass="h3">Advertisers</Panel.Title>
                </Panel.Heading>
                <Panel.Body>
                    <Button bsStyle="default" className="ProviderButton" onClick={() => this.setState({selectedProvider: null})}>
                        All
                    </Button>
                </Panel.Body>
                {
                    this.state.providersResults.map(
                        provider =>
                            <Panel.Body>
                                <Button bsStyle="default" className="ProviderButton" onClick={() => this.setState({selectedProvider: provider.id})}>
                                    {provider.name}
                                </Button>
                            </Panel.Body>
                    )
                }
            </Panel>
        </div>
    }

    renderDateSelector() {
        return <div className="col-md-2">
            <Panel bsStyle="default" className="DatePanel">
                <Panel.Heading>
                    <Panel.Title componentClass="h3">Date</Panel.Title>
                </Panel.Heading>
                <Panel.Body>
                    <DatePicker className="DatePicker"
                                dateFormat="yyyy-MM-dd"
                                selected={this.state.selectedDate}
                                onChange={this.handleChange}
                                placeholderText="select a date"/>
                </Panel.Body>
            </Panel>
        </div>
    }

    renderProductDataDetails() {
        return <div className="col-md-6">
            <ProductDataDetails productId={this.state.selectedProduct} providerId={this.state.selectedProvider} date={this.state.selectedDate}/>
        </div>
    }

    render() {
        if (this.state.productDataResults == null || this.state.productsResults == null || this.state.providersResults == null) {
            return (<p>Loading data</p>)
        }
        return (
            <div className="ProductDataPage">
                {this.renderProductSelector()}
                {this.renderProviderSelector()}
                {this.renderDateSelector()}
                {this.renderProductDataDetails()}
            </div>
        )
    }
}