import React from 'react';
import TestRenderer from 'react-test-renderer';
import ProductDataDetails from './ProductDataDetails';

it("verify render and props values", () => {
    const testRenderer = TestRenderer.create(<ProductDataDetails
        productId="1"
        providerId="5"
        date="2019-04-01"/>);

    const testInstance = testRenderer.root;
    expect(testInstance.props.productId).toBe('1');
    expect(testInstance.props.providerId).toBe('5');
    expect(testInstance.props.date).toBe('2019-04-01');
});