package indexer

import (
	"context"

	"github.com/algorand/go-algorand-sdk/client/v2/common"
	"github.com/algorand/go-algorand-sdk/client/v2/common/models"
)

// SearchForAssets /v2/assets
// Search for assets.
type SearchForAssets struct {
	c *Client
	p models.SearchForAssetsParams
}

// AssetID asset ID
func (s *SearchForAssets) AssetID(assetID uint64) *SearchForAssets {
	s.p.AssetID = assetID
	return s
}

// Creator filter just assets with the given creator address.
func (s *SearchForAssets) Creator(creator string) *SearchForAssets {
	s.p.Creator = creator
	return s
}

// Limit maximum number of results to return.
func (s *SearchForAssets) Limit(limit uint64) *SearchForAssets {
	s.p.Limit = limit
	return s
}

// Name filter just assets with the given name.
func (s *SearchForAssets) Name(name string) *SearchForAssets {
	s.p.Name = name
	return s
}

// Next the next page of results. Use the next token provided by the previous
// results.
func (s *SearchForAssets) Next(next string) *SearchForAssets {
	s.p.Next = next
	return s
}

// Unit filter just assets with the given unit.
func (s *SearchForAssets) Unit(unit string) *SearchForAssets {
	s.p.Unit = unit
	return s
}

// Do performs HTTP request
func (s *SearchForAssets) Do(ctx context.Context,
	headers ...*common.Header) (response models.AssetsResponse, err error) {
	err = s.c.get(ctx, &response,
		"/v2/assets", s.p, headers)
	return
}
