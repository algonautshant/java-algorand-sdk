package indexer

import (
	"context"
	"fmt"

	"github.com/algorand/go-algorand-sdk/client/v2/common"
	"github.com/algorand/go-algorand-sdk/client/v2/common/models"
)

// LookupAssetByID /v2/assets/{asset-id}
// Lookup asset information.
type LookupAssetByID struct {
	c       *Client
	assetId uint64
}

// Do performs HTTP request
func (s *LookupAssetByID) Do(ctx context.Context,
	headers ...*common.Header) (response models.AssetResponse, err error) {
	err = s.c.get(ctx, &response,
		fmt.Sprintf("/v2/assets/%d", s.assetId), nil, headers)
	return
}
