import React from 'react';
import TestRenderer from 'react-test-renderer';
import ProductDataPage from './ProductDataPage';

it("verify render", () => {
    TestRenderer.create(<ProductDataPage/>);
});