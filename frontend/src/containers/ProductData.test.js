import React from 'react';
import TestRenderer from 'react-test-renderer';
import ProductData from './ProductData';

it("verify render and props values", () => {
    const testRenderer = TestRenderer.create(<ProductData
        key='header'
        product='Product'
        provider='Advertiser'
        date='Date'
        clicks='Clicks'/>);

    const testInstance = testRenderer.root;
    expect(testInstance.props.product).toBe('Product');
    expect(testInstance.props.provider).toBe('Advertiser');
    expect(testInstance.props.date).toBe('Date');
    expect(testInstance.props.clicks).toBe('Clicks');
});