import React from 'react';

import './ProductDataRow.css';
import './ProductDataColumn.css';

const productData = (props) => (
    <tr className="ProductDataRow">
        <td className="ProductDataColumn">{props.product}</td>
        <td className="ProductDataColumn">{props.provider}</td>
        <td className="ProductDataColumn">{props.date}</td>
        <td className="ProductDataColumn">{props.clicks}</td>
    </tr>
);

export default productData;